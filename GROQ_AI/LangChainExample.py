from langchain_groq import ChatGroq
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser

llm=ChatGroq(model="openai/gpt-oss-120b",temperature=0.9)

prompt=ChatPromptTemplate.from_template(
    """You are a senior QA engineer.
    Generate test cases for the following feature:
    
    {feature}
    
    Include positive,negative,boundary and security scenarios.
    Return the result as a table with colums:
    ID|Scenario|Steps|Expectes Result"""
)
chain=prompt|llm|StrOutputParser()
feature="Login page with username and password.Password must be 8-16 charscters."
result=chain.invoke({"feature":feature})

print(result)