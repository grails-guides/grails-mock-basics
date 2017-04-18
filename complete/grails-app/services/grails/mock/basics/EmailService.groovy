package grails.mock.basics

import groovy.util.logging.Slf4j

@Slf4j
class EmailService {

    int sendEmail(Map message) {
        log.info 'Send email: {0}', message
        1
    }
}
