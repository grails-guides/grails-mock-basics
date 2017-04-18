package grails.mock.basics

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@Slf4j
@CompileStatic
class EmailService {

    int sendEmail(Map message) {
        log.info 'Send email: {0}', message
        1
    }
}
