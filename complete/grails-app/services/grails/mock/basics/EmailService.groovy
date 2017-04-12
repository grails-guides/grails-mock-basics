package grails.mock.basics

/**
 * Created by Nirav Assar on 4/12/2017.
 */
class EmailService {

    void sendEmail(Map message) {
        println "Send email: $message"
    }
}
