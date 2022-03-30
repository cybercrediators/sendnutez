import sqlite3

passt = open("passt.txt")
header = True

# nute table
category = "category"
nutrient = "name"
unit = "unit"

# nute_reference table
pop = "target_population"
age_from = "age_from"
age_to = "age_to"
gender = "gender"
fitness_value = "fitness_value"
reference_value = "reference_value"
upper_limit = "upper_limit"

categories = ["category", "nutrient", "target_population", "age_from", "age_to", "gender", "fitness_value", "reference_value"]
with open('passt.txt', 'w') as f:
    f.truncate(0)  # clear file
    for line in open("DRVs_All_populations.txt").readlines():
        if "Both genders" in line:
            f.writelines(line.replace("Both genders", "Male"))
            f.writelines(line.replace("Both genders", "Female"))
        else:
            f.writelines(line)


def getUnit(val: str):
    val = val.replace("?", "mü")
    if "/day" in val:
        return val.split("/day")[0].strip()
    if "per day" in val:
        return val.split("per day")[0].strip()
    return val.strip()


def fillRefVals(d, arr):
    # 0 = AI average requirement approx.
    # 1 = AR average requirement of half the population
    # 2 = PRI what people actually eat
    # 3 = RI % per energy need of target group
    # 4 = UL upper limit before unhealthy
    # 5 "save and adequate intake" use as fallback

    ref_str = ""
    # try to get a reference value 1 > 0 > 5 > 2
    if "ND" not in arr[1] and "NA" not in arr[1] and arr[1] is not None:
        ref_str = arr[1]
    elif "ND" not in arr[0] and "NA" not in arr[0] and arr[0] is not None:
        ref_str = arr[0]
    elif len(arr) > 5 and "ND" not in arr[5] and "NA" not in arr[5] and arr[5] is not None:
        ref_str = arr[5]
    elif "ND" not in arr[2] and "NA" not in arr[2] and arr[2] is not None:
        ref_str = arr[2]

    # other unit
    if "ND" not in arr[3] and "NA" not in arr[3] and arr[3] is not None:
        ref_str = arr[3]
    if ref_str != "" and ref_str != "\n":
        val_str = ref_str.split(" ")[0]
        if "-" in val_str:
            a = float(val_str.split("-")[0])
            b = float(val_str.split("-")[1])
            d[reference_value] = (a+b)/2.0
        elif "–" in val_str:
            a = float(val_str.split("–")[0])
            b = float(val_str.split("–")[1])
            d[reference_value] = (a+b)/2.0
        elif "ALAP" in val_str:
            d[reference_value] = 0.0
        else:
            d[reference_value] = float(val_str)
        d[unit] = getUnit(ref_str[len(val_str):])

        if "ND" not in arr[4] and "NA" not in arr[4] and arr[4] is not None:
            d[upper_limit] = arr[4].split(" ")[0]
        else:
            d[upper_limit] = 0.0

entries = []

for line in open("passt.txt").readlines():
    if header:
        header = False
    else:
        arr = line.split("\t")
        skip = False
        if "women" in arr[2]:
            skip = True
        if "LPI" in arr[2]:
            if "LPI 600" not in arr[2]:
                skip = True

        if not skip:
            d = dict()
            d[category] = arr[0]
            d[fitness_value] = 0.0
            d[nutrient] = arr[1]
            d[pop] = arr[2].split(" ")[0]

            if "month" in arr[3]:
                age_str = arr[3].split(" ")[0]
                if "-" in age_str:
                    d[age_from] = int(age_str.split("-")[0])
                    d[age_to] = int(age_str.split("-")[1])
                else:
                    d[age_from] = int(age_str)
                    d[age_to] = int(age_str)+1
            elif "year" in arr[3]:
                if "PAL" in arr[3]:
                    d[fitness_value] = float(arr[3].split("PAL=")[1])
                if ">" in arr[3]:
                    age_str = arr[3].split(" ")[1]
                    d[age_from] = int(age_str)*12
                    d[age_to] = 99*12
                else:
                    age_str = arr[3].split(" ")[0]
                    if "-" in age_str:
                        d[age_from] = int(age_str.split("-")[0])*12
                        d[age_to] = int(age_str.split("-")[1])*12
                    elif "–" in age_str:
                        d[age_from] = int(age_str.split("–")[0])*12
                        d[age_to] = int(age_str.split("–")[1])*12
                    else:
                        d[age_from] = (int(age_str))*12
                        d[age_to] = (int(age_str)+1)*12

            d[gender] = arr[4]
            fillRefVals(d, arr[5:])
            #print(d)
            entries.append(d)

con = sqlite3.connect('send_nutez.db')
create_nutes = "CREATE TABLE \"NUTE\" (" \
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," \
                "\"category\" TEXT," \
                "\"name\" TEXT," \
                "\"unit\" TEXT);"
create_reference_vals = "CREATE TABLE \"NUTE_REFERENCE_VALUE\" (" \
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," \
                "\"NUTE_ID\" INTEGER NOT NULL ," \
                "\"target_population\" TEXT," \
                "\"age_from\" INTEGER NOT NULL ," \
                "\"age_to\" INTEGER NOT NULL ," \
                "\"gender\" TEXT," \
                "\"fitness_value\" REAL NOT NULL ," \
                "\"reference_value\" REAL NOT NULL ," \
                "\"upper_limit\" REAL NOT NULL );"

c = con.cursor()
c.execute(create_nutes)
c.execute(create_reference_vals)
con.commit()
print()
print()
print()

u_names = []
for entry in entries:
    if entry[nutrient] not in u_names:
        u_names.append(entry[nutrient])
        if unit not in entry.keys():
            entry[unit] = ""
        print(u_names)
        strr = "INSERT INTO NUTE (_id, category, name, unit) VALUES (NULL, \"" + entry[category] + "\", \"" + entry[nutrient] + "\", \"" + entry[unit] + "\");"
        #print(strr)
        c.execute(strr)
        con.commit()
    c.execute("SELECT * FROM NUTE WHERE(name=\"" + entry[nutrient] + "\");")
    nute_id = c.fetchone()
    #print(entry[nutrient], nute_id)
    if reference_value in entry.keys():
        strr = "INSERT INTO NUTE_REFERENCE_VALUE VALUES (NULL," + str(nute_id[0]) + ",\"" + entry[pop] + "\", " + str(entry[age_from]) + ", " + str(entry[age_to]) + ", \"" + entry[gender] + "\", " + str(entry[fitness_value]) + ", " + str(entry[reference_value]) + ", " + str(entry[upper_limit]) + ");"
        c.execute(strr)
        con.commit()
