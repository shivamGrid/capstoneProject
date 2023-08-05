from fastapi import FastAPI,Request
import requests
from pydantic import BaseModel
from pymongo import MongoClient
from twilio.rest import Client
import langchain
import projfunc_copy_2 as pc2
import time
import json
import xml.etree.ElementTree as ET
from langchain.chat_models import ChatOpenAI
from gensim.models import Word2Vec,KeyedVectors
from langchain.embeddings.openai import OpenAIEmbeddings
from langchain.text_splitter import CharacterTextSplitter
from langchain.vectorstores import Chroma
from langchain.docstore.document import Document
from langchain.prompts import PromptTemplate
from langchain.indexes.vectorstore import VectorstoreIndexCreator
from langchain.embeddings.openai import OpenAIEmbeddings
from langchain.embeddings.cohere import CohereEmbeddings
from langchain.text_splitter import CharacterTextSplitter
from langchain.vectorstores.elastic_vector_search import ElasticVectorSearch
from langchain.vectorstores import Chroma
from langchain.chains.question_answering import load_qa_chain
from langchain.llms import OpenAI
from langchain.prompts import PromptTemplate
from langchain.memory import ConversationBufferMemory
from langchain.docstore.document import Document

client = MongoClient("mongodb://localhost:27017")
db = client["e_commerce"]
collection = db["products"]
data= list(collection.find())

my_dict_titles = {}
titles = []
documentfolder_filepath = "/Users/vmanikanta/Downloads/CAp_stone_pr/documents"
titles_filepath = "/Users/vmanikanta/Downloads/CAp_stone_pr/documents/titles.txt"
json_data_path = "/Users/vmanikanta/Downloads/e_commerceqwertyu.json"

data = pc2.loadjson(json_data_path)

my_dict_titles, titles = pc2.createproducttitile(data,documentfolder_filepath,my_dict_titles,titles)
title1 = "delivery charges and delivery related"
titles.append(title1)
my_dict_titles[title1] = "/Users/vmanikanta/Downloads/CAp_stone_pr/documents/delivery charges and delivery related.txt"
title2 = "Faq's on the returns and refunds"
titles.append(title2)
my_dict_titles[title2]= "/Users/vmanikanta/Downloads/CAp_stone_pr/documents/returns and refunds.txt"
titles3 = "No appropriate answer found as the given product is not present in the document list."
my_dict_titles[titles3]= "/Users/vmanikanta/Downloads/CAp_stone_pr/documents/notitle.txt"
title4 = "order related and delivery slots and timings"
titles.append(title4)
my_dict_titles[title4] = "/Users/vmanikanta/Downloads/CAp_stone_pr/documents/order related and delivery slots.txt"
pc2.createtitlesdocument(titles_filepath,titles)

model = pc2.train_titles(titles)
product_title_vecs = pc2.title_vec(titles,model)
#pc2.save_word2vec_vectors('/Users/vmanikanta/Downloads/CAp_stone_pr/documents/product_title_vecs.txt',product_title_vecs)
#model.save('/Users/vmanikanta/Downloads/CAp_stone_pr/documents/model.bin')

app = FastAPI()

class TextRequest(BaseModel):
    text: str
    phone_number: str
    start_time: str

class sendrequest(BaseModel):
    output_text: str


chat_history =""
memory = ConversationBufferMemory(memory_key="chat_history", input_key="question")
product_ids=[]
quantities =[]
previousreply = ""
addquan= []
addlen = 0
added =[]
ngrokurl = "https://11bb-14-143-15-250.ngrok-free.app"
@app.get('/')
def hello():
    return 'Hello World.'

@app.post('/test')
def testing(request: TextRequest):
    question = request.text
    phonenum = request.phone_number
    starttime = request.start_time
    data2 ={}
    data2['phn'] = phonenum
    data2['startTime']=starttime
    data2['human'] =question
    data2['bot'] = " hi, there!, how can you help you today"
    
    requests.post("https://da35-61-246-192-170.ngrok-free.app/send-message",json = data2)


@app.post("/webhook")
async def webhook_handler(request: TextRequest):
    global chat_history,memory,product_ids,quantities,ngrokurl,added,addlen,addquan,previousreply
    z=0
    prodid = []
    question = request.text
    phonenum = request.phone_number
    userid = request.phone_number
    userid = userid.replace("whatsapp:", "")
    starttime = request.start_time
    name = ""
    print("previousreply: ",previousreply)
    res = requests.get(f"{ngrokurl}/users/{userid}")
    if res.headers.get("Content-Type") == "application/json":
        json_data = res.json()
        username = json_data["username"]
        name = f"{username}"
    else: 
        print("no user name")
        name = " no user name please ask user name"
    #text = pc2.firsttemp(question,previousreply)
    #text = text.lstrip()
    #print(text,"\n")
    #time.sleep(3)
    text = pc2.all_functions(question)
    print(text)
    embeddings = OpenAIEmbeddings()
    final_titles =[]
    if text == 'normal_question': 
        #normal text
        content = pc2.emptyfile()
        print(content)
    elif text =='ingredients_and_recipe':
        previousreply = pc2.ingre_and_recipe(question)
        content = [Document(page_content='', metadata={'source': 0})]
        content[0].page_content = previousreply

        
    elif text =='confirming_products':
        json_pro = pc2.conf_pro(question,previousreply)
        print(json_pro)
        final_titles,quantities,pro_names = pc2.top5products(json_pro,product_title_vecs,titles,question,model)
        print(final_titles)
        z=1
        
    elif text == 'checkout':
        #checkout 
        resp = requests.get(f"{ngrokurl}/cart/retrieve-cart/{userid}")
        if resp.status_code == 200:
            texts = "provide address"
            content = [Document(page_content='', metadata={'source': 0})]
            content[0].page_content = texts
        else: 
            texts = "no cart details present not able to checkout, please ask user to add something."
            content = [Document(page_content='', metadata={'source': 0})]
            content[0].page_content = texts
        
    elif text == 'cancel_order':
        #cancel order
        texts = "cancelled order"
        content = [Document(page_content='', metadata={'source': 0})]
        content[0].page_content = texts
    elif text == 'get_name':
        #adding name of the user
        dta ={}
        dta["userId"] = userid
        name = pc2.fiftemp(question)
        #time.sleep(1)
        name = name.lstrip()
        print(name)
        dta["username"] = name
        requests.post(f"{ngrokurl}/users",json = dta)
        texts = "given name of the user in human_name"
        content = [Document(page_content='', metadata={'source': 0})]
        content[0].page_content = texts
    elif text == 'add_to_cart' or text =='remove_product_from_cart': 
        # add or remove
        if(text == 'add_to_cart'):
            if(addlen==0):
                texts = f"please ask any products that you are looking for."
                content = [Document(page_content='', metadata={'source': 0})]
                content[0].page_content = texts
            else:
                lastids = product_ids[-addlen:]
                lastquan = quantities[-addlen:]
                for pids,quan in zip(lastids,lastquan):
                    print(pids,quan)
                    added.append(pids)
                    addquan.append(quan)
                    data4 ={}
                    data4['productId'] = pids
                    data4['quantity']= quan
                    requests.post(f"{ngrokurl}/cart/{userid}",json = data4) 
                texts = "items added to cart.\n"
                texts += "Updated cart details are: \n"
                
                cartresponce = requests.get(f"{ngrokurl}/cart/retrieve-cart/{userid}")
                cart_data = cartresponce.json()
                print(cart_data)
                for item,i in zip(cart_data["cartItems"],range(len(cart_data["cartItems"]))):
                    productId = item["productId"]
                    quantity = item["quantity"]
                    texts += f"Product {i+1}: {productId}, Quantity: {quantity}\n"
                total = cart_data["total"]
                texts += f"Total cost: {total}"
                content = [Document(page_content='', metadata={'source': 0})]
                content[0].page_content = texts
                addlen = 0        
        else: 
            if(len(added)>0 ):
                remtext = pc2.remtemp(question)
                print(remtext)
                if(pc2.is_json(remtext)):
                    remdata = json.loads(remtext)
                    remids = [product["id"] for product in remdata["productids"]]
                    if(pc2.check_numbers(remids,len(added))):
                        
                        for i in range(len(remids)):
                            data6= {}
                            print(added[int(remids[i])-1],addquan[int(remids[i])-1])
                            data6['productId'] = added[int(remids[i])-1]
                            data6['quantity'] = addquan[int(remids[i])-1]
                            requests.put(f"{ngrokurl}/cart/{userid}/update",json = data6)
                        for j in range(len(remids)):
                            del added[int(remids[j])-1]
                            del addquan[int(remids[j])-1]
                            

                        texts = "items removed from cart\n"
                        texts += "Updated cart details are: \n"
                        print("time started")
                        time.sleep(10)
                        cartresponce = requests.get(f"{ngrokurl}/cart/retrieve-cart/{userid}")
                        cart_data = cartresponce.json()
                        print(cart_data)
                        for item,i in zip(cart_data["cartItems"],range(len(cart_data["cartItems"]))):
                            productId = item["productId"]
                            quantity = item["quantity"]
                            texts += f"Product {i+1}: {productId}, Quantity: {quantity}\n"
                        total = cart_data["total"]
                        texts += f"Total cost: {total}"
                        content = [Document(page_content='', metadata={'source': 0})]
                        content[0].page_content = texts
                    else:
                        texts = "sorry, some of the products you want to remove is out of your length of your cart, so please specify it again by correcting your mistake then i can remove. "
                        content = [Document(page_content='', metadata={'source': 0})]
                        content[0].page_content = texts

                        
                else:
                    remtext = remtext.lstrip()
                    texts = remtext
                    content = [Document(page_content='', metadata={'source': 0})]
                    content[0].page_content = texts

    elif(text =='get_cart_details'):
        #view cart
        responce = requests.get(f"{ngrokurl}/cart/retrieve-cart/{userid}")
        content = "Your cart details: \n"
        if responce.status_code == 200:
            cart_data = responce.json()
            print(cart_data)

            # Extract productId, quantity, and total
            
            for item,i in zip(cart_data["cartItems"],range(len(cart_data["cartItems"]))):
                productId = item["productId"]
                quantity = item["quantity"]
                content += f"Product {i+1}: {productId}, Quantity: {quantity}\n"

            total = cart_data["total"]
            content += f"Total cost: {total}"
        else:
            content = "no cart details to get cart information, tell the user there are no products in cart please add something."
        print(content)
        text_splitter = CharacterTextSplitter(chunk_size=1000, chunk_overlap=0)
        texts = text_splitter.split_text(content)
        docsearch = Chroma.from_texts(texts, embeddings, metadatas=[{"source": i} for i in range(len(texts))])
        content = docsearch.similarity_search(question)

    elif(text=='get_order_details'):
        #view order
        responce = requests.get(f"{ngrokurl}/cart/retrieve-order/{userid}")
        content = "Your order details: \n"
        
        if responce.status_code == 200:
            print(responce.status_code)
            cart_data = responce.json()
            print(cart_data)
            # Extract productId, quantity, and total
            
            for item,i in zip(cart_data["cartItems"],range(len(cart_data["cartItems"]))):
                productId = item["productId"]
                quantity = item["quantity"]
                content += f"Product {i+1}: {productId}, Quantity: {quantity}\n"
            orderId = cart_data["orderId"]
            content+= f"orderId: {orderId}"
            status = cart_data["status"]
            if(status == 'Processing'):
                content += "Order status: Your order is placed and confirmed and it is processing to dispatch for delivery\n"
            else:
                f"Order status: {status}\n"
            total = cart_data["total"]
            content += f"Total cost: {total}"
        else:
            content = "no order details to get cart information, tell the user there are no orders created."
        print(content)
        text_splitter = CharacterTextSplitter(chunk_size=1000, chunk_overlap=0)
        texts = text_splitter.split_text(content)
        docsearch = Chroma.from_texts(texts, embeddings, metadatas=[{"source": i} for i in range(len(texts))])
        content = docsearch.similarity_search(question)

    elif(text=='feedback'):
        #feedback
        data6 = {}
        data6["userId"] = userid
        data6["feedback"] = question
        requests.post(f"{ngrokurl}/feedback",json = data6)
        texts = "feedback"
        content = [Document(page_content='', metadata={'source': 0})]
        content[0].page_content = texts


    elif(text =='address'):
        #address and order placing
        respo = requests.get(f"{ngrokurl}/cart/retrieve-cart/{userid}")
        if respo.status_code == 200:
            requests.post(f"{ngrokurl}/cart/{userid}/checkout",data = question)
            texts = "order placed"
            content = [Document(page_content='', metadata={'source': 0})]
            content[0].page_content = texts
        else: 
            texts = "no cart details present not able to checkout, please ask user to add something"
            content = [Document(page_content='', metadata={'source': 0})]
            content[0].page_content = texts
        
        
    elif pc2.is_json(text): 
        #product retrival
        final_titles,quantities = pc2.top5products(text,product_title_vecs,titles,question,model)
        print(final_titles)
        z=1
    elif text == 'faqs': 
        #document retrival
        final_titles = pc2.finaltitles(text,product_title_vecs,titles,model)
        print(final_titles)     
    else:
        #normal text
        content = pc2.emptyfile()
        print(content)
    print("\n")
    
    documents = []
    
    if(len(final_titles)>0):
        addlen = 0
        for i in range(len(final_titles)):
            print(final_titles[i])
            print(my_dict_titles[final_titles[i]])
            
            context = pc2.opentextfile(my_dict_titles[final_titles[i]])
            if(z==1 and context!="No appropriate answer found as the given product is not present in the database, please ask user to modify the search for this product."):
                data_dict = json.loads(context)
                addlen +=1 
                oid_value = data_dict["_id"]["$oid"]
                prodid.append(oid_value)
                product_ids.append(oid_value)
            context = f"For {pro_names[i]}: " + context
            documents.append(context)
        docsearch = Chroma.from_texts(documents, embeddings, metadatas=[{"source": str(i)} for i in range(len(documents))]).as_retriever()
        content = docsearch.get_relevant_documents(question)
        content[len(content)-1].page_content += "\n\nshow this items to the user, do not answer added to cart, total cost of items, u can just tell cost of each item, and ask user to do you add these items to cart."
    
    x, chat_history,memory = pc2.thirdtemp(content,question,memory,name)
    data2 ={}
    data2['phn'] = phonenum
    data2['startTime']=starttime
    data2['human']= question
    data2['bot'] = x['output_text']
    data2['productIdImage'] = prodid
    if text =='ingredients_and_recipe':
        previousreply += x['output_text']
    else: 
        previousreply = x['output_text']
    requests.post(f"{ngrokurl}/send-message",json = data2)
    prodid =[]
    print(x)
    return x