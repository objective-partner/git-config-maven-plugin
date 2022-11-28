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
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/** */
@Mojo(name = "hooksPath")
public class HooksPathMojo extends AbstractMojo {

  private File gitRepoPath;
  private File gitHooksPath;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
      Repository repo = repoBuilder.setGitDir(gitRepoPath).readEnvironment().findGitDir().build();
      StoredConfig config = repo.getConfig();
      config.setString("core", null, "hooksPath", gitHooksPath.toString());
      config.save();
    } catch (Exception e) {
      throw new MojoFailureException(e);
    }
  }

  /** */
  @Parameter(readonly = true, defaultValue = "${basedir}/.git")
  public void setGitRepoPath(File gitRepoPath) {
    this.gitRepoPath = gitRepoPath;
  }

  /** */
  @Parameter(property = "hooksPath.hooksPath", defaultValue = "${basedir}/git-hooks")
  public void setGitHooksPath(File gitHooksPath) {
    this.gitHooksPath = gitHooksPath;
  }
}
