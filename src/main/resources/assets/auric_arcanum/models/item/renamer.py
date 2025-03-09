import os

colors = [
    "mud",
    "clay",
    "ice",
    "magma"
]

location = input("Where?")

for root, dirs, files in os.walk(location):
    for file in files:
        if "stone" in file:
            file_path = os.path.join(root, file)
            print(file)
            for color in colors:
                new_file_name = file.replace("stone", color)
                new_file_path = os.path.join(root, new_file_name)
                
                with open(file_path, 'r') as f:
                    data = f.read()
                
                # Replace "white" with the current color in the file content
                data = data.replace("stone", color)
                
                # Write the updated data to the new file
                with open(new_file_path, 'w') as f:
                    f.write(data)


