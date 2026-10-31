"""A simple menu-driven chatbot for home automation."""


DEVICES = {
    "1": {
        "name": "Lights",
        "locations": ["Living room", "Bedroom", "Kitchen"],
        "actions": ["turn on", "turn off"],
    },
    "2": {
        "name": "Fan",
        "locations": ["Living room"],
        "actions": ["low", "medium", "high"],
    },
    "3": {
        "name": "Door locks",
        "locations": ["Front door", "Back door"],
        "actions": ["lock", "unlock"],
    },
    "4": {
        "name": "Air Conditioner",
        "locations": ["Living room", "Bedroom"],
        "actions": ["set temperature"],
    },
}


def welcome(name):
    print(f"Welcome, {name}! I'm your home automation assistant.")
    print(
        "I can help you manage lights, fans, door locks, and "
        "air conditioners."
    )
    print("Choose an option from the main menu to get started.")


def next_device_id():
    return str(max((int(device_id) for device_id in DEVICES), default=0) + 1)


def show_devices():
    print("\nAvailable devices")
    for device_id, device in DEVICES.items():
        print(f"{device_id}: Control {device['name']}")


def choose_menu_option(prompt, valid_options):
    while True:
        choice = input(prompt).strip().lower()
        if choice in valid_options:
            return choice
        if not choice:
            print("A choice is required. Please try again.")
        else:
            print(f"Option {choice} isn't available. Please choose again.")


def choose_location(locations):
    while True:
        for index, location in enumerate(locations, start=1):
            print(f"{index}: {location}")
        print("0: Return to main menu")
        choice = input("Choose a device: ").strip()
        if not choice:
            print("A device choice is required. Please try again.")
            continue
        if choice == "0":
            return None
        if choice.isdigit() and 1 <= int(choice) <= len(locations):
            return locations[int(choice) - 1]
        print(f"Option {choice} isn't available. Please choose again.")


def control_device(device):
    print(f"\n{device['name']}")
    location = choose_location(device["locations"])
    if location is None:
        return

    print("Available actions")
    for index, action in enumerate(device["actions"], start=1):
        print(f"{index}: {action}")
    action_choice = choose_menu_option(
        "Choose an action: ",
        {str(index) for index in range(1, len(device["actions"]) + 1)},
    )
    action = device["actions"][int(action_choice) - 1]

    if device["name"] == "Air Conditioner" and action == "set temperature":
        while True:
            temperature = input("Enter a target temperature in °C (5-32): ").strip()
            if not temperature:
                print("Temperature is required. Please enter a value from 5°C to 32°C.")
                continue
            try:
                target = float(temperature)
            except ValueError:
                print("Please enter a numeric temperature.")
                continue
            if not 5 <= target <= 32:
                print("Warning: temperature must be between 5°C and 32°C.")
                continue
            print(f"Simulated: set the {location} {device['name'].lower()} to {target:g}°C.")
            return

    print(f"Simulated: {action} {location} {device['name'].lower()}.")


def add_device():
    while True:
        name = input("Enter the new device name: ").strip()
        if name:
            break
        print("A device name is required.")

    while True:
        locations = [
            location.strip()
            for location in input("Enter one or more locations, separated by commas: ").split(",")
            if location.strip()
        ]
        if locations:
            break
        print("At least one location is required.")

    while True:
        actions = [
            action.strip()
            for action in input("Enter actions, separated by commas: ").split(",")
            if action.strip()
        ]
        if actions:
            break
        print("At least one action is required.")

    device_id = next_device_id()
    DEVICES[device_id] = {
        "name": name,
        "locations": locations,
        "actions": actions,
    }
    print(f"Added {name} as device {device_id}.")


def add_device_action():
    if not DEVICES:
        print("There are no devices to update.")
        return

    show_devices()
    device_id = choose_menu_option(
        "Choose a device to update (or 0 to cancel): ",
        {"0", *DEVICES.keys()},
    )
    if device_id == "0":
        return

    while True:
        action = input("Enter the new action: ").strip()
        if not action:
            print("An action is required.")
        elif action.casefold() in {
            existing.casefold() for existing in DEVICES[device_id]["actions"]
        }:
            print("That action already exists for this device.")
        else:
            DEVICES[device_id]["actions"].append(action)
            print(f"Added '{action}' to {DEVICES[device_id]['name']}.")
            return


def list_device_details():
    if not DEVICES:
        print("No devices have been configured.")
        return
    for device in DEVICES.values():
        print(f"\n{device['name']}")
        print(f"  Locations: {', '.join(device['locations'])}")
        print(f"  Actions: {', '.join(device['actions'])}")


def control_device_menu():
    show_devices()
    device_id = choose_menu_option(
        "Choose a device to control (or 0 to return): ",
        {"0", *DEVICES.keys()},
    )
    if device_id != "0":
        control_device(DEVICES[device_id])


def user_menu():
    while True:
        print("\nUser menu")
        show_devices()
        print("0: Exit")
        choice = choose_menu_option("Choose a device or exit: ", {"0", *DEVICES.keys()})
        if choice == "0":
            return
        control_device(DEVICES[choice])


def admin_menu():
    while True:
        print("\nAdmin menu")
        print("1: Add a device")
        print("2: Add an action to a device")
        print("3: List device details")
        print("4: Control a device")
        print("5: Exit")
        choice = choose_menu_option("Choose an option (1-5): ", {"1", "2", "3", "4", "5"})
        if choice == "1":
            add_device()
        elif choice == "2":
            add_device_action()
        elif choice == "3":
            list_device_details()
        elif choice == "4":
            control_device_menu()
        else:
            return


def main():
    try:
        name = input("What is your name? ").strip() or "there"
        while True:
            role = input("Choose your role (user/admin): ").strip().lower()
            if role in ("user", "admin"):
                break
            if not role:
                print("A role is required. Please enter user or admin.")
            else:
                print("Invalid role. Please enter user or admin.")

        welcome(name)
        if role == "admin":
            admin_menu()
        else:
            user_menu()
        print(f"Goodbye, {name}! Have a great day.")
    except (EOFError, KeyboardInterrupt):
        print("Input canceled. Goodbye!")


if __name__ == "__main__":
    main()