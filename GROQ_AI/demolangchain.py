from langchain_groq import ChatGroq
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import JsonOutputParser

llm = ChatGroq(model="openai/gpt-oss-120b", temperature=0.9)
parser = JsonOutputParser()

prompt = ChatPromptTemplate.from_template(
    """You are a senior QA automation engineer.
Create Selenium automation for the feature below. Cover positive, negative,
boundary, and security validation scenarios. Security scenarios must be safe
validation checks, such as rejecting invalid input; do not perform attacks.

Return one valid JSON object matching this shape:
{{
  "test_cases": [
    {{
      "id": "TC-001",
      "category": "positive | negative | boundary | security",
      "scenario": "Short description",
      "steps": ["Action 1", "Action 2"],
      "expected_result": "Expected behavior"
    }}
  ],
  "selenium_pytest_code": "A complete Python pytest module implementing these test cases with Selenium"
}}

In the Selenium module, use pytest and selenium.webdriver, explicit
WebDriverWait waits, and By locators. Do not use time.sleep. Since page URLs
and selectors are not provided, define clearly named placeholders for them
and explain them in Python comments. Read the base URL from the BASE_URL
environment variable and do not hardcode real credentials. Make password
boundary tests cover 7, 8, 16, and 17 characters. Return only valid JSON.

{format_instructions}

Feature:
{feature}"""
)
chain = prompt | llm | parser
feature = "Login page with username and password. Password must be 8-16 characters."
result = chain.invoke({
    "feature": feature,
    "format_instructions": parser.get_format_instructions(),
})

print(result)