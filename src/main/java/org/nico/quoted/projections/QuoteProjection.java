package org.nico.quoted.projections;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
import org.springframework.data.rest.core.config.Projection;

// TODO currently used to expose the source field of a quote - not needed but easier to read
@Projection(name = "quote", types = { Quote.class })
public
interface QuoteProjection {
    long getId();
    String getText();
    Source getSource();
}
