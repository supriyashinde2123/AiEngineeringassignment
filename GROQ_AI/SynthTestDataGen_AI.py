from langchain_groq import ChatGroq
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import JsonOutputParser
llm = ChatGroq(model="openai/gpt-oss-120b", temperature=0)
prompt = ChatPromptTemplate.from_template(
    """You are a test data generator.
    Generate{count}realistic user records for and indian e-commarce app.
    
    Rules:
    -Fields: name,emails,phone,city,age
    -Email must match the person's name 
    -Phone must be a 10-digit Indian mobile number
    -Include 1 invalid record(e.g.bad email are age belows 18)and
    add a field "is_valid": false for it; use true for the others 
    
    Return ONLY a JSON list.No extra text."""
    )
chain=prompt|llm|JsonOutputParser()
users=chain.invoke({"count":5})
for user in users:
    print(user)