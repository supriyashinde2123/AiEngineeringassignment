from langchain.agents import create_agent
from langchain_groq import ChatGroq
from langchain_core.tools import tool

@tool
def get_weather(city: str) -> str:
    """Get the weather for a city."""
    # Placeholder function to simulate fetching weather data
    return f"It is 28°C and sunny in {city}."


@tool
def multiply(a: float, b: float) -> float:
    """Multiply two numbers."""
    return a * b

llm = ChatGroq(model="openai/gpt-oss-120b", temperature=0)

agent=create_agent(
    model=llm,
    tools=[get_weather, multiply],
    system_prompt="You are a  helpful assistant.use tools when needed.",
)
result=agent.invoke(
    {"messages":[{"role":"user","content":"What is 12.5*8?"}]}
)
print(result["messages"][-1].content)
