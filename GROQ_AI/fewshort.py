import json

from langchain_groq import ChatGroq
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import JsonOutputParser

count = 5
llm = ChatGroq(model="openai/gpt-oss-120b", temperature=0)
prompt = ChatPromptTemplate.from_template(
    """Generate exactly {count} test rows for this SQL table:
    CREATE TABLE users (
        id INT PRIMARY KEY,
        name VARCHAR(50) NOT NULL,
        email VARCHAR(100) UNIQUE,
        age INT CHECK (age >= 18),
        city VARCHAR(30)
    );

    Example row: {{"id": 1, "name": "anita rao", "email": "anita.rao@example.com", "age": 29, "city": "pune"}}

    Follow that JSON object shape. Use realistic Indian names and cities, unique integer
    IDs and email addresses, and values within the column lengths. Return only a JSON
    array containing exactly {count} objects with fields id, name, email, age, and city.
    Exactly one row must have an integer age below 18; all other ages must be 18 or older.
    Keep every other value valid for the table."""
)
chain = prompt | llm | JsonOutputParser()
users = chain.invoke({"count": count})
print(json.dumps(users, indent=2))
