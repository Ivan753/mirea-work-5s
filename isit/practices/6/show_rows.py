#!/usr/bin/env python3
# -*- coding: utf-8

import cgi
import cgitb
import pymysql.cursors

cgitb.enable() # debagging only


print("Content-Type: text/html;charset=utf-8")
print()


connection = pymysql.connect(
    host='localhost',
    user='admin',
    password='',
    db='db_bugaev',
    charset='utf8mb4',
    cursorclass=pymysql.cursors.DictCursor
)

# getting variant
variant = 1
f = cgi.FieldStorage()
for i in f.keys():
    if i == 'variant' and int(f[i].value) in (1, 2):
        variant = int(f[i].value)

print("""
<!doctype html>
<html>
    <head>
        <title>Список файлов</title>
    </head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" crossorigin="anonymous">
    <body class="container">
""")

if variant == 1:
    print("<h1>Полный список</h1>")
else:
    print("<h1>Только ID и Path</h1>")

with connection.cursor() as cursor:
    if variant == 1:
        sql = "SELECT * FROM student_tbl_files"
        cursor.execute(sql,)
        result = cursor.fetchall()
        print("""
        <table class="table mt-3">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Path</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Share_link</th>
                    <th>id_user</th>
                    <th>date_add</th>
                </tr>
            </thead>
            <tbody>
        """)
        for row in result:
            print(f"""
            <tr>
                <td>{row['id']}</td>
                <td>{row['path']}</td>
                <td>{row['name']}</td>
                <td>{row['description']}</td>
                <td>{row['share_link']}</td>
                <td>{row['id_user']}</td>
                <td>{row['date_add']}</td>
            </tr>"""
            )
        print("</tbody>")
        print("</table>")
    else:
        sql = "SELECT id, path FROM student_tbl_files"
        cursor.execute(sql,)
        result = cursor.fetchall()
        print("""
        <table class="table mt-3">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Path</th>
                </tr>
            </thead>
            <tbody>
        """)
        for row in result:
            print(f"<tr><td>{row['id']}</td><td>{row['path']}</td></tr>")
        print("</tbody>")
        print("</table>")

print("""
    </body>
</html>
""")

connection.close();
