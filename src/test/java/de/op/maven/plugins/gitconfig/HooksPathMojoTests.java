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
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Path;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class HooksPathMojoTests {

  private HooksPathMojo mojo;
  private File gitHooksPath;
  private File gitRepoPath;

  @BeforeEach
  void setUp(@TempDir Path projectDir) {
    gitHooksPath = projectDir.resolve("git-hooks").toFile();
    gitRepoPath = projectDir.resolve(".git").toFile();

    mojo = new HooksPathMojo();
    mojo.setGitHooksPath(gitHooksPath);
    mojo.setGitRepoPath(gitRepoPath);
  }

  @Test
  void testSetHooksPath(@TempDir Path projectDir) throws Exception {
    FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
    Repository repo = repoBuilder.setGitDir(gitRepoPath).readEnvironment().findGitDir().build();
    repo.create();

    mojo.execute();

    String hooksPath = repo.getConfig().getString("core", null, "hooksPath");
    assertThat(hooksPath).isEqualTo(gitHooksPath.toString());
  }
}
