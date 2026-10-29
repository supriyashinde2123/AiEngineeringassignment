from langchain_groq import ChatGroq
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import JsonOutputParser

count = 5
schema = """CREATE TABLE users (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    age INT CHECK (age >= 18),
    city VARCHAR(30)
);"""

llm = ChatGroq(model="openai/gpt-oss-120b", temperature=0)
prompt = ChatPromptTemplate.from_template(
    """Generate exactly {count} test rows for this SQL table schema:
    {schema}

    Example row: {{"id": 1, "name": "anita rao", "email": "anita.rao@example.com", "age": 29, "city": "pune"}}

    Infer the columns, data types, and constraints from the schema. Return only a JSON
    array of exactly {count} objects, with keys matching the table columns. Use realistic
    Indian names and cities, unique integer IDs and email addresses, and values within
    the declared lengths. Exactly one row must intentionally violate only the age check
    by having an integer age below 18; all other rows must satisfy every constraint."""
)
chain = prompt | llm | JsonOutputParser()
users = chain.invoke({"count": count, "schema": schema})


