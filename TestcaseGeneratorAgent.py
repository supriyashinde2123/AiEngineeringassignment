import os
from langchain_groq import ChatGroq
from langchain.agents import create_agent
from langchain_core.tools import tool

@tool
def save_java_file(filename: str, content: str) -> str:
    """Save the given content to a Java file."""
    os.makedirs("tests", exist_ok=True)
    path = os.path.join("tests", os.path.basename(filename))
    with open(path, "w", encoding="utf-8") as file:
        file.write(content)
    return f"Saved {path}"


llm = ChatGroq(model="openai/gpt-oss-120b", temperature=0)
agent = create_agent(
    model=llm,
    tools=[save_java_file],
    system_prompt=(
        "You are a senior QA engineer. Write Selenium WebDriver tests in Java "
        "using JUnit 5. Use only the locators given by the user. "
        "Write one complete test class, save it as one Java file with the "
        "save_java_file tool, and reply with a one-line summary of the tests created."
    ),
)

task = """
Create Selenium Java tests for https://www.saucedemo.com.
Use these locators:
- Username: id=user-name
- Password: id=password
- Login button: id=login-button
- Error message: css=[data-test='error']
- Inventory list: class=inventory_list

Test cases:
1. Valid login (standard_user/secret_sauce) shows the inventory list.
2. Wrong password shows an error message.
3. Empty username shows an error message.

Create one test class named LoginTests and save it as LoginTests.java.
"""
result = agent.invoke({"messages": [{"role": "user", "content": task}]})
print(result["messages"][-1].content)
