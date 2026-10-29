import json

from langchain_groq import ChatGroq
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import JsonOutputParser

llm = ChatGroq(model="openai/gpt-oss-120b", temperature=0.9)
parser = JsonOutputParser()

prompt = ChatPromptTemplate.from_template(
    """You are a senior QA engineer.
Generate test cases for this feature, including positive, negative, boundary, and security scenarios.

Return a JSON object with a "test_cases" array. Each test case must have:
- id
- category
- scenario
- steps (an array of strings)
- expected_result

{format_instructions}

Feature:
{feature}
"""
)

chain = prompt | llm | parser

feature = "Login page with username and password. Password must be 8–16 characters."
result = chain.invoke({
    "feature": feature,
    "format_instructions": parser.get_format_instructions(),
})

print(json.dumps(result, indent=2))