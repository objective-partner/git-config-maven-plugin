package de.op.maven.plugins.gitconfig;

/*-
 * #%L
 * git-config-maven-plugin
 * %%
 * Copyright (C) 2022 objective partner AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/** */
@Mojo(name = "gitHooksPath")
public class HooksPathMojo extends AbstractMojo {

  private File gitRepoPath;
  private File gitHooksPath;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      setAndSaveHooksPath();
    } catch (RepositoryNotFoundException e) {
      getLog()
          .error(
              "Please initialize a git repository before try to set a hooksPath for this project.");
      throw new MojoFailureException(e);
    } catch (IOException e) {
      throw new MojoExecutionException(e);
    }
  }

  private void setAndSaveHooksPath() throws IOException {
    FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
    try (Repository repo =
        repoBuilder
            .setGitDir(gitRepoPath)
            .readEnvironment()
            .findGitDir()
            .setMustExist(true)
            .build()) {
      StoredConfig config = repo.getConfig();
      config.setString("core", null, "hooksPath", gitHooksPath.toString());
      config.save();
    }
  }

  /** */
  @Parameter(property = "gitHooksPath.repoPath", defaultValue = "${basedir}/.git")
  public void setGitRepoPath(File gitRepoPath) {
    this.gitRepoPath = gitRepoPath;
  }

  /** */
  @Parameter(property = "gitHooksPath.hooksPath", defaultValue = "${basedir}/git-hooks")
  public void setGitHooksPath(File gitHooksPath) {
    this.gitHooksPath = gitHooksPath;
  }
}
