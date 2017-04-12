package grails.mock.basics

import com.fasterxml.jackson.databind.BeanProperty
import grails.transaction.Transactional
import org.hibernate.validator.constraints.Email

@Transactional
class ClassroomService {

    EmailService emailService

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

    void emailStudents(Classroom classroom) {
        List<Student> students = classroom.students as List<Student>
        for (student in students) {
            emailService.sendEmail(to: "${student.name}", from: "${classroom.teacher}",
                    note: "Your grade is ${student.grade}")
        }
    }
}
