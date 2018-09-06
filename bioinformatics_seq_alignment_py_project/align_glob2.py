table = []
table_width = 0
table_height = 0
seq1 = ""
seq2 = ""


def init():
    global table
    global table_width
    global table_height
    global seq1
    global seq2

    seq1 = "AGTGCTA"
    seq2 = "GTGTAC"

    print("The first sequence is:", seq1)
    print("The second sequence is:", seq2)

    table_width = len(seq1) + 1
    table_height = len(seq2) + 1

    table = [[0 for y in range(table_height)] for x in range(table_width)]

    for x in range(0, table_width):
        table[x][0] = 0 - x

    for y in range(0, table_height):
        table[0][y] = 0 - y


def print_table():
    global table
    global table_width
    global table_height

    for y in range(0, table_height):
        for x in range(0, table_width):
            print(table[x][y], end='\t')
        print(' ')


def fill_cell(x, y):
    global table
    global seq1
    global seq2

    diag = table[x - 1][y - 1]
    if seq1[x - 1] == seq2[y - 1]:
        diag = diag + 1

    gap_x = table[x - 1][y] - 1
    gap_y = table[x][y - 1] - 1

    max_val = diag
    if gap_x > max_val:
        max_val = gap_x
    if gap_y > max_val:
        max_val = gap_y

    table[x][y] = max_val


def next_path_step(x, y):
    global table
    global seq1

    step = [0, 0, ""]

    if x > 0:
        if table[x - 1][y] - 1 == table[x][y]:
            step[0] = x - 1
            step[1] = y
            step[2] = "-"
            return step

    if y > 0:
        if table[x][y - 1] - 1 == table[x][y]:
            step[0] = x
            step[1] = y - 1
            step[2] = "-"
            return step

    if x > 0 and y > 0:
        if table[x - 1][y - 1] + 1 == table[x][y]:
            step[0] = x - 1
            step[1] = y - 1
            step[2] = seq1[x - 1]
            return step

        if table[x - 1][y - 1] == table[x][y]:
            step[0] = x - 1
            step[1] = y - 1
            step[2] = "-"
            return step

    return step


def create_alignment():
    global table
    global table_width
    global table_height

    alignment = ""
    table_pos_x = table_width - 1
    table_pos_y = table_height - 1

    while table_pos_x != 0 or table_pos_y != 0:
        step = next_path_step(table_pos_x, table_pos_y)
        print(step)
        table_pos_x = step[0]
        table_pos_y = step[1]
        alignment = alignment + step[2]

    return alignment[::-1]


def run():
    init()

    print("\nInitial table: ")
    print_table()

    for y in range(1, table_height):
        for x in range(1, table_width):
            fill_cell(x, y)

    print("\nFilled table: ")
    print_table()

    print("\nCalculated alignment: ")
    print(create_alignment())


run()
