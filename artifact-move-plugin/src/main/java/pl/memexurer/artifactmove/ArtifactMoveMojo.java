package pl.memexurer.artifactmove;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "artifact-move")
public class ArtifactMoveMojo extends AbstractMojo {

  @Parameter(property = "artifact-move.from")
  private File from;

  @Parameter(property = "artifact-move.to")
  private File to;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      if(!from.exists())
        throw new IOException("File `from` does not exist!");

      this.getLog().info("Moving " + from.getAbsolutePath() + " to " + to.getAbsolutePath());
      Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not move file: " + e.getMessage(), e);
    }
  }
}
