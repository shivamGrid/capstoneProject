import io
import json
import os
import openai
from dotenv import load_dotenv
import numpy as np
import pickle
import time
from langchain.chat_models import ChatOpenAI
from gensim.models import Word2Vec
from langchain.chains.question_answering import load_qa_chain
from langchain.llms import OpenAI
from langchain.prompts import PromptTemplate
from langchain.memory import ConversationBufferMemory
from langchain.chains.question_answering import load_qa_chain
from langchain.llms import OpenAI
from langchain.prompts import PromptTemplate
from langchain.memory import ConversationBufferMemory
#os.environ["OPENAI_API_KEY"] = "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"
#os.environ["OPENAI_API_KEY"] = "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"
load_dotenv()
os.getenv("OPENAI_API_KEY")

from langchain.llms import OpenAI
from langchain import PromptTemplate, LLMChain
davinci = OpenAI(model_name='text-davinci-003',temperature = 0)
#gpt = OpenAI(model_name='gpt-3.5-turbo',temperature = 0)

import nltk
from nltk.corpus import stopwords

stop_words = set(stopwords.words('english'))
additional_words = ["buy", "give", "want", "please"]
# Convert the set of stop words to a list
stop_words_list = list(stop_words)
# Add the additional words to the list
stop_words_list.extend(additional_words)
# Convert the list back to a set
stop_words = set(stop_words_list)

def createpickle(filepath,data):
    with open(filepath, 'wb') as file:
        pickle.dump(data, file)

def loadpickle(filepath):
    with open(filepath, 'rb') as file:
        loaded_data = pickle.load(file)
    return loaded_data

def opentextfile(filepath):
# Open the file in read mode
    with open(filepath, 'r') as file:
        # Read the contents of the file
        contents = file.read()
    return contents



def preprocess(text):
    tokens = nltk.word_tokenize(text.lower())
    tokens = [token for token in tokens if token.isalpha()]
    tokens = [token for token in tokens if token not in stop_words]
    return tokens

def train_titles(titles):

    title_tokens = [preprocess(title) for title in titles]
    model = Word2Vec(title_tokens, min_count=1)
    return model

def encode(query, model):
    query_tokens = preprocess(query)
    query_vec = sum([model.wv[token] for token in query_tokens]) / len(query_tokens)
    return query_vec

def title_vec(titles,model):
    product_title_vecs = [encode(title,model) for title in titles]
    return product_title_vecs

def save_word2vec_vectors(file_path, vectors):
    with open(file_path, 'w') as f:
        for vector in vectors:
            # Convert the vector values to strings
            vector_str = ' '.join(str(value) for value in vector)

            # Write the vector to the file
            f.write(vector_str + '\n')

def load_word2vec_vectors(file_path):
    vectors = []
    with open(file_path, 'r') as f:
        for line in f:
            # Split the line into individual values
            values = line.strip().split(' ')

            # Convert the values to floats and create a vector
            vector = [float(value) for value in values]

            # Add the vector to the list
            vectors.append(vector)

    return vectors



def createtextfile(filepath,data):
    file = open(filepath, "w")
    file.write(data)
    file.close()

def createjsonfile(filepath,text):
    with open(filepath, 'w') as file:
        # Convert the dictionary to a string using JSON
        json_data = json.dumps(text)

        # Write the string representation to the file
        file.write(json_data)


def is_json(text):
    try:
        json.loads(text)
    except ValueError:
        return False
    return True

def loadjson(filepath):
    with open(filepath) as f:
        data = json.load(f)
        return data
    
def createproducttitile(data,filepath,my_dict_titles,titles):
    for product in data:
        if 'brand' in product and 'product' in product and 'sub_category' in product:
            title = f"{product['sub_category']}: {product['product']} by {product['brand']}"
            if title not in titles:
                titles.append(title)
                my_dict_titles[title] = f"{filepath}/product{product['index']}.txt"
                text = product
                createjsonfile(my_dict_titles[title],text)
    return my_dict_titles,titles

def creatextitle(filepath,my_dict_titles,title,title_data,titles):
    my_dict_titles[title] = f"{filepath}/{title}.txt"
    titles.append(title)
    createtextfile(my_dict_titles[title],title_data)
    return my_dict_titles,titles

def createtitlesdocument(filepath,titles):
    with open(filepath, 'w') as f:
        for doc in titles:
            f.write("%s\n" % doc)

def emptyfile():
    filename = "/Users/vmanikanta/Downloads/CAp_stone_pr/empty_file.txt"
    # Open the text file in read mode
    with open(filename, 'r') as file:
        content = file.read()
    return content

def check_numbers(numbers, length):
    for number in numbers:
        if not (0 < int(number) <= length):
            return False
    return True
def all_functions(question):
    functions = [
        
        {
            "name": "get_name",
            "description": "Get the name of the user,question tells about human that he is giving/telling his name but not for asking/remebering his name",
            "parameters": {
                "type": "object",
                "properties": {
                    "name": {
                        "type": "string",
                        "description": "name of user",
                    },},
                "required": ["name"],
            },
        },
        {
            "name": "get_order_details",
            "description": "Get the order details if user asks about his order.",
            "parameters": {
                "type": "object",
                "properties": {},
                "required": [],
            },
        },
        {
            "name": "normal_question",
            "description": "Handle generic greetings or non-specific questions..",
            "parameters": {
                "type": "object",
                "properties": {},
                "required": [],
            },
        },
        {
            "name": "add_to_cart",
            "description": "question is confirming to the add to cart",
            "parameters": {
                "type": "object",
                "properties": {},
                "required": [],
            },
        },
        {
            "name": "checkout",
            "description": "question is confirming to the checkout for the cart",
            "parameters": {
                "type": "object",
                "properties": {},
                "required": [],
            },
        },
        {
            "name": "faqs",
            "description": "Get the delivery charges, delivery slots, return policy, refund and retuns, order related faqs, delivery timings.",
            "parameters": {
                "type": "object",
                "properties": {},
                "required": [],
            },
        },
        {
            "name": "address",
            "description": "if question looks like address or giving address details.",
            "parameters": {
                "type": "object",
                "properties": {},
                "required": [],
            },
        },
        {
            "name": "feedback",
            "description": "question tells about proper Feedback from the human or any compliments or review.",
            "parameters": {
                "type": "object",
                "properties": {},
                "required": [],
            },
        },
        {
            "name": "cancel_order",
            "description": "for cancelling orders.",
            "parameters": {
                "type": "object",
                "properties": {},
                "required": [],
            },
        },
        {
            "name": "remove_products_from_cart",
            "description": "question tells confirmation that to remove any product from cart only",
            "parameters": {
                "type": "object",
                "properties": {},
                "required": [],
            },
        },
        {
            "name": "get_cart_details",
            "description": "get the cart details.",
            "parameters": {
                "type": "object",
                "properties": {},
                "required": [],
            },
        },
        {
        "name": "ingredients_and_recipe",
        "description": "handling for question asked for making recipe or getting ingredients for mentioned dish.",
        "parameters": {
            "type": "object",
            "properties": {},
            "required": [],
            },
        },
        {
            "name": "confirming_products",
            "description": "if user wants to get or buy or purchase the products mentioned in the question, user need not to be explicitly mention all the products in the question, maybe he may mention give all products or give all products except this items or give particular items.",
            "parameters": {
                "type": "object",
                "properties": {},
                "required": [],
            },
        }
    ]
    

    model_name = "gpt-3.5-turbo-0613"


    response = openai.ChatCompletion.create(
        model=model_name,
        messages=[
            {"role": "user", "content": question},
        ],
        functions=functions,
        function_call="auto",
    )

    function_name = response['choices'][0]['message']['function_call']['name']
    
    return function_name

def ingre_and_recipe(question):
    # build prompt template for simple question-answering
    template = """you are grocery store chatbot, give the ingredients of the dish mentioned in the question in JSON format like key as products and corresponding values as quantities in integer, do not show water in ingridients unless question tells to show water too, after json output give the recipe along with the ingredients which are required for that particular recipe in a proper format.
    Question: {question}
    Answer: """
    prompt = PromptTemplate(template=template, input_variables=["question"])
    temperature =0
    llm_chain = LLMChain(
        prompt=prompt,
        llm=davinci
    )
    return llm_chain.run({'question': question})
def conf_pro(question,previousreply):
    # build prompt template for simple question-answering
    template = """you are grocery store chatbot, check products mentioned in the previous reply, please give the the products mentioned in the question in json format with respect to previous reply, do not modify product name and quantity

    ---------------------
    {previousreply}
    ---------------------

    ---------------------
    {question}
    ---------------------

    Answer: """
    prompt = PromptTemplate(template=template, input_variables=["question","previousreply"])
    temperature =0
    llm_chain = LLMChain(
        prompt=prompt,
        llm=davinci
    )

    return (llm_chain.run({'question': question,'previousreply':previousreply}))
            
def fiftemp(question):
    template = """you will be given question that contains name of the user, such that answer should return only name extracted from question.

    Question: {question}

    Answer: """

    prompt = PromptTemplate(template=template, input_variables=["question"])
    
    llm_chain = LLMChain(
        prompt=prompt,
        llm=davinci
    )

    text = llm_chain.run(question)
    return text

def remtemp(query):
    template = """you will be given query from human. \
    query is about removing items from the cart.\
    for example,\
    if query is " i want to remove product 5 and product2" you need to send me products 5 and 2 in JSON format.\
    the JSON should follow this steps: The outermost structure is a dictionary with a key "productids" denoted by curly brackets.and key value of 'productids' is a list with square brackets. list contains multiple items, each item within "productids" list is an object represented by curly braces.\
    and object consist of key-value pair, where key is a string and the value is a string, the key is 'id' and value should be product id for example if query is product 1 or product1 then value is "1".\
    
    if query does not contain any information like which product number to remove, then return "please provide the product number to remove, like remove product1 or product2." but you should not return this in JSON.

    Query: {query}

    Answer: """

    prompt = PromptTemplate(template=template, input_variables=["query"])
    
    llm_chain = LLMChain(
        prompt=prompt,
        llm=davinci
    )

    text = llm_chain.run(query)
    return text

def firsttemp(question,previousreply):
    # build prompt template for simple question-answering
    template = """you are given previousreply and question, previousreply was chat history of another llm chain that replies as AI for the question by human, \n
    let me tell you how this should work : the order of previousreply of another llm is as follows: first human asks question then you will find correct answer and i will send to another llm chain, llm chain ai replies based on your answer it will detect,and then question from human is given along with previous reply to you again the same repeats.\n 
    so u now understand that i am using you as a middle ware to detect the correct answer that i mentioned in the below if else instructions.\n\n

    first find if there are any spelling mistakes in the question such as product names spelling mistakes, and remember those spelling mistakes and correct it while you are evaluating or giving responce
    second while answering you need not answer the products mentioned in previousreply or chat history for JSON format, you just need to answer products for the question asked, because u don't know the database which i am fetching for the products
    third you can read whole previous reply and question and find relation between the current question the human with last messege from AI from given previousreply u can use this relation while giving responce if else conditions and then predict the correct answer from the if else instructions below.
    
    if(question is about buying or getting products only and but not for delivery or return related questions):Answer from the question in which it should answer all the product names , quantity seperately and mentioned in question in the JSON format. Example if query: "Could you please give me 2 loafs of bread of britania" json output of products object should contains product key, quantity key, unit key, brand if question does not contain quantity give default quantity as 1 and unit as NA, brand as NA, JSON should satify the below conditions
    The outesrmost structure is a dictionary with a key "products" denoted by curly brackets. and key value of 'products' is a list with square brackets. It contains multiple elements or items, each represented by curly braces. In this case, the list is named 'products' and contains four items.
    Each item within the 'products' list is an object represented by curly braces. Objects consist of key-value pairs where the key is a string and the value can be of various types.
    The keys within the objects are strings that represent a specific attribute or property of the item. In this case, the keys are 'product,' 'quantity,' 'unit', and 'brand.'
    The values associated with the keys can be of different types. In this case, the values can be strings or numbers, depending on the key.
    it should work even if question contains only product also you should be able generate products list.
    
    else if(question is about regarding details related to grocery store like delivery or return policies and delivery timings): you need to answer the following: for delivery charges and delivery related question answer 'delivery', for return and refund related question answer 'returns and refunds', for order related and delivery slots question answer 'order related and slots', do not return in JSON format and also answer those i mentioned in Apostrophe.
    
    else if(whole relation tells confirmation about proceeding to checkout only not adding to cart): return 3
    
    else if(question tells no to anything): return 0

    else if(relation tells confirmation that to add to cart only, not for proceed to checkout): return 1

    else if(question tells about viewing the cart): return 4

    else if(relation tells about the Address of the human): return 5

    else if(relation or question tells about proper Feedback from the human or any compliments or review): return 6

    else if(question tells about viewing order details): return 7
    
    else if(question tells about cancelling order): return 8

    else if(relation tells confirmation that to remove any product from cart only): return 10

    else if(question tells about human that he is giving/telling his name but not for asking/remebering his name): return 9

    else : return 0.
   
   
    --------------------------------------
    Question: {question}
    --------------------------------------
    --------------------------------------
    Previousreply: {previousreply}
    --------------------------------------
    
    Answer: """
    prompt = PromptTemplate(template=template, input_variables=["question","previousreply"])

    llm_chain = LLMChain(
        prompt=prompt,
        llm=davinci
    )
    inputs = {
        "question": question,
        "previousreply": previousreply
    }
    text = llm_chain.run(inputs)
    return text

def top5products(text,product_title_vecs,titles,query,model):
    # Parse the JSON data
    content =[]
    ans = []
    data = json.loads(text)

    # Extract product names
    #product_names = [product["name"] for product in data["products"]]
    product_names = []
    quantities = []

    for product, quantity in data.items():
        product_names.append(product)
        quantities.append(quantity)

    for word in product_names:
        query = word
        query_vec = encode(query,model)
        similarities = np.dot(product_title_vecs, query_vec) / (np.linalg.norm(product_title_vecs, axis=1) * np.linalg.norm(query_vec))
    #     most_similar_index = np.argmax(similarities)

    #     most_similar_product_title = product_title[most_similar_index]
    #     print(most_similar_product_title)
        most_similar_indices = np.argsort(similarities)[::-1][:5] # get top 5 indices in descending order
        
        print(f"Top 5 most similar product titles for query '{query}':")
        for index in most_similar_indices:
            most_similar_product_title = titles[index]
            print(most_similar_product_title,index)
            content.append(most_similar_product_title)
        #time.sleep(3)
        print("\n")
        answer = fourthtemp(content,query,word)
        answer = answer.lstrip()
        print(answer)
        ans.append(answer)
    
    return ans,quantities,product_names

def finaltitles(text,product_title_vecs,titles,model):
    content = []
    query_vec = encode(text,model)
    similarities = np.dot(product_title_vecs, query_vec) / (np.linalg.norm(product_title_vecs, axis=1) * np.linalg.norm(query_vec))
    most_similar_index = np.argmax(similarities)

    most_similar_title = titles[most_similar_index]
    content.append(most_similar_title)
    return content

def fourthtemp(document,question,product):
    # build prompt template for simple question-answering
    template = """Document list contains 5 sentences of product titles for the product given to you, now follow the steps below:\
        step 1: find the product given to you and verify it is also asked in the question if not there skip the steps below, \
        step 2: now understand the product title among the 5 titles mentioned in the document list to the given product name, \
        step 3: select and output one product title for product that is similar match or appropriate answer to the product, \
        step 4. do not skip the words, do not modify the product title string and do not add any typographical symbols and punctuations in that product title, return the whole product title sentence. because that product title is used a key for the dictionary, so if you change the sentence of the product title it will give KeyError\
            for example you have product title as this: Salt, Sugar & Jaggery: Pure Refined - Sugar, Sulphur Free/Sakkare by Parry's ,you need to return whole string Salt, Sugar & Jaggery: Pure Refined - Sugar, Sulphur Free/Sakkare by Parry's for not getting keyerror. 
            example 2 if i have "Men's Grooming: NHT 1091 Pro Cut Cordless Trimmer For MenÂ - Black by Nova 20508" you need to send whole string without modifing, do not do like this "Men's Grooming: NHT 1091 Pro Cut Cordless Trimmer For MenÂ\xa0- Black by Nova" \
        step 5: if no product title matches return "No appropriate answer found as the given product is not present in the document list.".
    Question: {question}
    Document: {document}
    Product: {product}
    Answer: """
    prompt = PromptTemplate(template=template, input_variables=["question","document","product"])
    llm_chain = LLMChain(
        prompt=prompt,
        llm=davinci,
    )
    return llm_chain.run({'question': question, 'document':  document,'product':product})

def thirdtemp(content, query,chat_memory,name):
    template = """You are given context, human_name, question as inputs,You are a chatting with a human, human name was given in triple Apostrophe. you will be answering the queries either based on context or question given and make use of if else conditions below, make sure u follow if else conditions and instrictions mentioned inside if else conditions below.
    if(question is like Hi, or hello or anything like this): if 'human_name' in the triple Apostrophe is empty string, ask the human or user to provide the name to proceed further, else you can answer using his name given in the triple Apostrophe.
    else if(context contains ingredients or instructions or recipe for making any dish):please answer same mentioned in the context and please add new lines in the context such that the text you answer should fit in the whatsapp conversation in a nice and proper format fits to whatsapp conversation in mobile.
    else if(question is about 'viewing cart' only not to 'add' or 'remove' items from cart): Understand the given context do not answer on your own, you need tell cart details using given context only and total cost of the cart in a proper format like while showing products for each product show it in a new line also total cost in new line, and ask them, do you want to proceed to checkout, if context is given that "no cart details to get cart information, tell the user there are no products in cart please add something." then give answer based on context given. do not use 'chat history' or 'memory' to get cart details.
    else if(question is about cancel order): cancel the order and do not ask anything about cancellation.
    else if(question and context is confirming yes to proceed to checkout only not adding to cart): please ask user to provide address details and wait for the address to be added by the user, if context is given that no cart details then tell the answer based on the context.
    else if(context tells item added to cart): tell items/item are added in the cart, in the new line you need to give updated cart details from the given context do not answer on your own, you need tell cart details using given context only and total cost of the cart in a proper format like while showing products for each product show it in a new line also total cost in new line. after this u can ask proceed to checkout or is there anything that you can help in ur own words.
    else if(question gives the 'Address' of the user): if context is given that "no cart details present not able to checkout, please ask user to add something." then give answer based on context given, if context is about order placed then just tell the user that "order has been placed sucessfully, it would be great if you provide feedback for us". 
    else if(question tells about the feedback from the user): greet the user for the feedback and end the chat by telling have a nice day.
    else if(question tells about viewing order details): Understand the given context, if order status is placed succesfully, then from the given context give details of the placed order in a proper format like while showing products for each product show it in a new line also order status in new line and do not tell do you want proceed to checkout again, if given context is "no order details to get cart information, tell the user there are no orders created." then tell order is not placed yet, do not use 'memory' or 'chat_history' to show order details.
    else if(context contains product id or context contains product information in JSON format): answer the product name and brand in a proper format like while showing products for each product show it in a new line, show price in rupees for each product individually from context in JSON format, do not answer product id or oid or index or total cost of all products because u does not know the exact quantity that i want to give do not give it from ur memory And add this text should i add to the cart?,  if context contains "No appropriate answer found as the given product is not present in the document list." then tell to modify query.
    else if(context contains text information): summarize the document according to the question, if context contains "No appropriate answer found as the given product is not present in the document list." then tell to modify query.
    else: give answer based on your knowledge.
    
    
    ---------------------------------------------
    
    {context}
    
    ---------------------------------------------
    
    _____________________________________________

    '''{human_name}'''
    _____________________________________________
    
    {chat_history}
    Human: {question}
    Chatbot: """

    prompt = PromptTemplate(
        input_variables=["chat_history", "question", "context","human_name"], 
        template=template
    )
    memory = chat_memory
    #print("before memory: ",memory)
    #memory = ConversationBufferMemory(memory_key="chat_history", input_key="question")
    chain = load_qa_chain(OpenAI(temperature=0,model_name = "text-davinci-003"), chain_type="stuff", memory=memory, prompt=prompt,verbose = True)
    x = chain({"input_documents": content, "question": query,"human_name":name}, return_only_outputs=True)
    y= chain.memory.buffer
    #print("after memory: ",memory)
    #print("\n y:",y)
    return x,y,memory





