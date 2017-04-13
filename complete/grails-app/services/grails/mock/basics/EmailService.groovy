package grails.mock.basics

class EmailService {

    int sendEmail(Map message) {
        println "Send email: $message"
        1
    }
}
