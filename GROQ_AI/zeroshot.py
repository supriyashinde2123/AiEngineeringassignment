import json

from langchain_groq import ChatGroq
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import JsonOutputParser

count = 5
llm = ChatGroq(model="openai/gpt-oss-120b", temperature=0)
prompt = ChatPromptTemplate.from_template(
    """You are a test data generator. Generate exactly {count} rows for this SQL table:
    CREATE TABLE users (
        id INT PRIMARY KEY,
        name VARCHAR(50) NOT NULL,
        email VARCHAR(100) UNIQUE,
        age INT CHECK (age >= 18),
        city VARCHAR(30)
    );

    Requirements:
    - Return objects with exactly these fields: id, name, email, age, city.
    - Use unique integer IDs and unique email addresses.
    - Use realistic Indian names and Indian cities.
    - Use an integer for age. Exactly one row must have age below 18, intentionally
      violating only the age check; every other row must have age 18 or older.
    - Keep values within the declared VARCHAR lengths and provide a non-empty name.
    - Return only a JSON array, with no extra text or validity fields."""
)
chain = prompt | llm | JsonOutputParser()
users = chain.invoke({"count": count})
