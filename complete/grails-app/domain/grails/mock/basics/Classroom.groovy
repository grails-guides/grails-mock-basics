package grails.mock.basics

class Classroom {

    String teacher
    static hasMany = [students: Student]

    static constraints = {
    }
}
