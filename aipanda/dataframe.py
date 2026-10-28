import pandas as pd

# If the file is on your local computer
df = pd.read_csv(r'C:\aipanda\student.csv')

# If the file is hosted online (e.g., GitHub, AWS S3)
# df = pd.read_csv('https://example.com')

attendance_column = "attendence_percentage"
marks_columns = ["unittest_1_marks", "unittest_2_marks"]

df = df.dropna(subset=[attendance_column, *marks_columns])
df = df[~df["rollno"].duplicated(keep=False)]
df = df[df[attendance_column] <= 100]


print(df.head())
print(df.tail())
print(df.shape)
df["name"]
df[["name","unittest_1_marks"]]
print(df.describe())
df.info()
