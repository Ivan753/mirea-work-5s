require('reflect-metadata');
const express = require('express');
const { createConnection } = require('typeorm');
const { WordCounter } = require('../lab3/word-counter');
const Student = require('./entity/Student');
const app = express();

const root = process.cwd() + '/pages';

/* ------------------------------------------ */

let connection;

createConnection({
    type: 'sqlite',
    synchronize: true,
    database: 'database.sqlite',
    entities: [
        Student
    ]
})
    .then(conn => { connection = conn; })
    .catch(err => console.log("Error ", err))

/* ------------------------------------------ */

app.use(express.static('static'));
app.use(express.json())

app.get('/', (req, res) => {
    res.sendFile('index.html', { root });
});

app.post('/api/count', (req, res) => {
    res.setHeader('Content-Type', 'application/json');

    if (req.body.words) {
        const counter = new WordCounter(req.body.words);
        res.send({ result: counter.getResult() });
    } else {
        res.send({ result: 0 });
    }
});

app.get('/about', (req, res) => {
    res.sendFile('about.html', { root });
});

app.get('/table', (req, res) => {
    res.sendFile('table.html', { root });
});

app.get('/api/getStudents', (req, res) => {
    res.setHeader('Content-Type', 'application/json');

    const StudentRepository = connection.getRepository(Student);

    StudentRepository.find().then((loadedStudents) => {
        const response = [];

        for (let i = 0; i < loadedStudents.length; i += 1) {
            const st = loadedStudents[i];

            response.push({
                id: st.id,
                name: st.name,
                city: st.city,
                course: st.course,
            })
        }

        return res.send({ students: response });
    })
    .catch((err) => {
        console.log('Error: ', err);
        res.statusCode = 500;
        res.send('');
    });
});

app.post('/api/addStudent', (req, res) => {
    res.setHeader('Content-Type', 'application/json');

    const StudentRepository = connection.getRepository(Student);

    const student = new Student();

    student.name = req.body.name;
    student.city = req.body.city;
    student.course = req.body.course;

    StudentRepository
        .save(student)
        .then((st) => res.send({ id: st.id}))
        .catch((err) => {
            console.log('Error: ', err);
            res.statusCode = 500;
            res.send('');
        })
});

app.post('/api/removeStudent', (req, res) => {
    const StudentRepository = connection.getRepository(Student);

    StudentRepository.findOne(req.body.id)
        .then((student) => StudentRepository.remove(student))
        .then(() => res.send('OK'))
        .catch((err) => {
            console.log('Error: ', err);
            res.statusCode = 500;
            res.send('');
        });
});

app.post('/api/updateStudent', (req, res) => {
    const StudentRepository = connection.getRepository(Student);

    StudentRepository.findOne(req.body.id).then((student) => {
        student.name = req.body.name;
        student.city = req.body.city;
        student.course = req.body.course;
        return StudentRepository.save(student);
    })
        .then(() => res.send('OK'))
        .catch((err) => {
            console.log('Error: ', err);
            res.statusCode = 500;
            res.send('');
        });
});


app.listen(8080, () => {
    console.log('Server listening on port 8080');
});
