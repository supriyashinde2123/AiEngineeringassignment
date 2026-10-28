user_context = {"name": input("Enter your name: ")}


def show_balance(name):
    print(f"--> {name}, your balance is 5,000.")


def open_credit_card_services(name):
    print(f"--> Opening Credit Card menu for {name}...")


def open_forex_card_services(name):
    print(f"--> Opening Forex Card menu for {name}...")


def exit_menu(name):
    print(f"--> Goodbye, {name}.")


while True:
    print(f"Hello {user_context['name']}, please choose an option:")
    print("1: Balance")
    print("2: Credit Card Services")
    print("3: Forex Card Services")
    print("4: Exit")
   
    choice = input("Enter choice (1-4): ")
    
    if choice == "1":
        show_balance(user_context["name"])
    elif choice == "2":
        open_credit_card_services(user_context["name"])
    elif choice == "3":
        open_forex_card_services(user_context["name"])
    elif choice == "4":
        exit_menu(user_context["name"])
        break 
    else:
        print("--> Invalid choice. Try again.")
        
        
        
