package grails.mock.basics

class EmailService {

    void sendEmail(Map message) {
        println "Send email: $message"
    }
}
