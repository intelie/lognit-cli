package net.intelie.lognit.cli.model;

import java.util.Collection;

public class Terms {
    private final Collection<String> terms;

    public Terms(Collection<String> terms) {
        this.terms = terms;
    }

    public Collection<String> getTerms() {
        return terms;
    }
}
