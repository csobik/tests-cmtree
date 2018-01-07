package net.csobik.cmtree.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jcommander.JCommanderParameterResolverAutoConfiguration;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.legacy.LegacyAdapterAutoConfiguration;

import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;

import net.csobik.cmtree.commands.CmTreeCommands;

/**
 * @author jirisobotik
 * @version 1.0.0
 * @since 06.01.18
 */
@Configuration
@Import({
            // Core runtime
            SpringShellAutoConfiguration.class,
            JLineShellAutoConfiguration.class,
            // Various Resolvers
            JCommanderParameterResolverAutoConfiguration.class,
            LegacyAdapterAutoConfiguration.class,
            StandardAPIAutoConfiguration.class,
            // Built-In Commands
            StandardCommandsAutoConfiguration.class,
            // Allows ${} support
            PropertyPlaceholderAutoConfiguration.class,
            CmTreeCommands.class,
            // Application Commands
//            JCommanderCommands.class,
//            LegacyCommands.class,

//            FileValueProvider.class,
//            DynamicCommands.class,
//            TableCommands.class,
})
public class NoAutoConf {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(NoAutoConf.class, args);
  }

}
