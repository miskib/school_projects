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

    seq1 = "ATTCGATCC"
    seq2 = "ACGAT"

    print("The first sequence is:", seq1)
    print("The second sequence is:", seq2)

    table_width = len(seq1) + 1
    table_height = len(seq2) + 1

    table = [[0 for y in range(table_height)] for x in range(table_width)]

    for x in range(0, table_width):
        table[x][0] = 0

    for y in range(0, table_height):
        table[0][y] = 0


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


def find_max_pos() :
    global table
    global table_width
    global table_height

    max_val = table[1][1]
    max_val_index_x = 1
    max_val_index_y = 1

    for y in range(1, table_height):
        for x in range(1, table_width):
            if table[x][y] > max_val :
                max_val = table[x][y]
                max_val_index_x = x
                max_val_index_y = y
    return [max_val_index_x, max_val_index_y]


def next_path_step(x, y):
    global table
    global seq_1

    step = [0, 0, "", 0]

    if x > 0:
        if table[x - 1][y] - 1 == table[x][y]:
            step[0] = x - 1
            step[1] = y
            step[2] = "-"
            step[3] = table[x - 1][y]
            return step

    if y > 0:
        if table[x][y - 1] - 1 == table[x][y]:
            step[0] = x
            step[1] = y - 1
            step[2] = "-"
            step[3] = table[x][y - 1]
            return step

    if x > 0 and y > 0:
        if table[x - 1][y - 1] + 1 == table[x][y]:
            step[0] = x - 1
            step[1] = y - 1
            step[2] = seq1[x - 1]
            step[3] = table[x - 1][y - 1]
            return step
        
        if table[x - 1][y - 1] == table[x][y]:
            step[0] = x - 1
            step[1] = y - 1
            step[2] = "-"
            step[3] = table[x - 1][y - 1]
            return step

    return step


def create_alignment():
    global table
    global table_width
    global table_height

    alignment = ""
    start_pos = find_max_pos()
    table_pos_x = start_pos[0]
    table_pos_y = start_pos[1]

    while table_pos_x != 0 and table_pos_y != 0:
        step = next_path_step(table_pos_x, table_pos_y)
        table_pos_x = step[0]
        table_pos_y = step[1]
        alignment = alignment + step[2]
        if step[3] == 0:
            break

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