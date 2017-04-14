package grails.mock.basics

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.hibernate.validator.constraints.Email
import spock.lang.Specification

@TestFor(ClassroomService)
@Mock([Student, Classroom])
class ClassroomServiceSpec extends Specification {

    //tag::studentsInfo[]
    static def studentsInfo = [ // <1>
            [name: "Nirav", grade: 91],
            [name: "Sergio", grade: 95],
            [name: "Jeff", grade: 93]
    ]

    //end::studentsInfo[]

    //tag::testFind[]
    void "test find students with grades above"() {
        given: "students are already stored in db"
        mockStudentsOnly()

        when: "service is called to search"
        List<Student> students = service.findStudentsWithGradeAbove(92) // <3>

        then: "students are found with appropriate grades"
        students.size() == 2
        students[0].name == "Sergio"
        students[0].grade == 95
        students[1].name == "Jeff"
        students[1].grade == 93
    }

    //end::testFind[]

    //tag::testCalc[]
    void "test calculate average grade of classroom"() {
        given: "students are part of a classroom"
        mockStudentsInAClassroom()

        when: "service is called to calculate an average"
        BigDecimal avgGrade = service.calculateAvgGrade("Smith")// <2>

        then: "the average grade is calculated"
        avgGrade == 93
    }

    //end::testCalc[]

    void "test email students with mock collaborator"() {
        given: "students are part of a classroom"
        Classroom classroom = mockStudentsInAClassroom()
        def mockEmailService = Mock(EmailService)
        service.emailService = mockEmailService


        when: "service is called to email students"
        int emailCount = service.emailStudents(classroom)

        then:
        1 * mockEmailService.sendEmail([to:"Sergio", from:"Smith", note:"Your grade is 95"]) >> 1
        1 * mockEmailService.sendEmail([to:"Nirav", from:"Smith", note:"Your grade is 91"]) >> 1
        1 * mockEmailService.sendEmail([to:"Jeff", from:"Smith", note:"Your grade is 93"]) >> 1
        emailCount == 3
    }

    void "test email students with expando"() {
        given: "students are part of a classroom"
        Classroom classroom = mockStudentsInAClassroom()
        def mockEmailService = new Expando()
        mockEmailService.sendEmail = { Map message ->
            return 1
        }
        service.emailService = mockEmailService


        when: "service is called to email students"
        int emailCount = service.emailStudents(classroom)

        then:
        emailCount == 3
    }

    void "test email students with metaclass replace"() {
        given: "students are part of a classroom"
        Classroom classroom = mockStudentsInAClassroom()
        EmailService mockEmailService = new EmailService()
        mockEmailService.metaClass.sendEmail = { Map message ->
            return 1
        }
        service.emailService = mockEmailService


        when: "service is called to email students"
        int emailCount = service.emailStudents(classroom)

        then:
        emailCount == 3
    }

    /***********************************************************************************/

    //tag::mockStudentsOnly[]
    void mockStudentsOnly() {
        for (s in studentsInfo) {
            new Student(name: s.name, grade: s.grade).save()// <2>
        }
    }

    //end::mockStudentsOnly[]

    //tag::mockStudentsInAClassroom[]
    Classroom mockStudentsInAClassroom() {
        Classroom classroom = new Classroom(teacher: "Smith")
        for (s in studentsInfo) {
            classroom.addToStudents(new Student(name: s.name, grade: s.grade))
        }
        classroom.save() // <1>
        classroom
    }

    //end::mockStudentsInAClassroom[]


}
