import re

import tiktoken

STOP_WORDS = {
    "a", "an", "and", "are", "as", "at", "be", "been", "being", "but",
    "by", "did", "do", "does", "for", "from", "had", "has", "have", "he",
    "her", "hers", "him", "his", "i", "if", "in", "into", "is", "it",
    "its", "me", "my", "of", "on", "or", "our", "ours", "she", "that",
    "the", "their", "theirs", "them", "they", "this", "those", "to", "was",
    "we", "were", "what", "when", "where", "which", "who", "will", "with",
    "you", "your", "yours",
}


def clean_prompt(prompt: str) -> str:
  
    return re.sub(r"\s+", " ", prompt.lower()).strip()


def remove_stopwords(prompt: str) -> str:
    
    filtered_prompt = re.sub(
        r"\b[\w']+\b",
        lambda match: (
            " " if match.group().casefold() in STOP_WORDS else match.group()
        ),
        clean_prompt(prompt),
    )
    return re.sub(r"\s+", " ", filtered_prompt).strip()


def tokenize_prompt(prompt: str, model: str = "gpt-4o") -> None:
    cleaned_prompt = clean_prompt(prompt)
    filtered_prompt = remove_stopwords(cleaned_prompt)
    encoding = tiktoken.encoding_for_model(model)
    token_ids = encoding.encode(filtered_prompt)

    print("Cleaned prompt:", cleaned_prompt)
    print("Filtered prompt:", filtered_prompt)
    print("Token IDs:", token_ids)
    print("Token count:", len(token_ids))
    print("Unique token count:", len(set(token_ids)))

if __name__ == "__main__":
    prompt = input("Enter a prompt: ")
    tokenize_prompt(prompt)