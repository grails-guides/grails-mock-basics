package grails.mock.basics

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ClassroomService)
@Mock([Student, Classroom])
class ClassroomServiceSpec extends Specification {

    static def studentsInfo = [
            [name: "Nirav", grade: 91],
            [name: "Sergio", grade: 95],
            [name: "Jeff", grade: 93]
    ]

    void "test find students with grades above"() {
        given: "students are already stored in db"
        mockStudentsOnly()

        when: "service is called to search"
        List<Student> students = service.findStudentsWithGradeAbove(92)

        then: "students are found with appropriate grades"
        students.size() == 2
        students[0].name == "Sergio"
        students[0].grade == 95
        students[1].name == "Jeff"
        students[1].grade == 93
    }

    void "test calculate average grade of classroom"() {
        given: "students are part of a classroom"
        mockStudentsInAClassroom()

        when: "service is called to calculate an average"
        BigDecimal avgGrade = service.calculateAvgGrade("Smith")

        then: "the average grade is calculated"
        avgGrade == 93
    }

    void mockStudentsOnly() {
        for (s in studentsInfo) {
            new Student(name: s.name, grade: s.grade).save()
        }
    }

    void mockStudentsInAClassroom() {
        Classroom classroom = new Classroom(teacher: "Smith")
        for (s in studentsInfo) {
            classroom.addToStudents(new Student(name: s.name, grade: s.grade))
        }
        classroom.save()
    }


}
