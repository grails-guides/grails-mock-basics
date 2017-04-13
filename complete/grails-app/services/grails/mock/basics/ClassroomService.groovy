package grails.mock.basics

class ClassroomService {

    def emailService

    List<Student> findStudentsWithGradeAbove(BigDecimal grade) {
        List<Student> students = Student.findAllByGradeGreaterThanEquals(grade)
        students
    }

    BigDecimal calculateAvgGrade(String teacher) {
        Classroom classroom = Classroom.findByTeacher(teacher)
        List<Student> students = classroom.students as List<Student>
        List<BigDecimal> grades = students.collect { it.grade }
        BigDecimal avgGrade = grades.sum() / students.size()
        avgGrade
    }

    int emailStudents(Classroom classroom) {
        List<Student> students = classroom.students as List<Student>
        int emailCount = 0
        for (student in students) {
            emailCount += emailService.sendEmail(to: "${student.name}", from: "${classroom.teacher}",
                    note: "Your grade is ${student.grade}")
        }
        emailCount
    }
}
