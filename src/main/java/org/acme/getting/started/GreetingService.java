package org.acme.getting.started;/**
 * @author Henrique Droog
 */

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Henrique Droog
 *
 * @since 2020-07-07
 */
@ApplicationScoped
public class GreetingService {

    public String greeting(String name) {
        return "hello " + name;
    }
}
