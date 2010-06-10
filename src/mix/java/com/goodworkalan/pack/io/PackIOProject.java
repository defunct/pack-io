package com.goodworkalan.pack.io.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

/**
 * Builds the project definition for Pack I/O.
 *
 * @author Alan Gutierrez
 */
public class PackIOProject implements ProjectModule {
    /**
     * Build the project definition for Pack I/O.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.pack/pack-io/0.1")
                .depends()
                    .production("com.github.bigeasy.pack/pack/0.+1")
                    .development("org.testng/testng-jdk15/5.10")
                    .end()
                .end()
            .end();
    }
}
