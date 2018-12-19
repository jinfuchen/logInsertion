package org.git;

import org.eclipse.jgit.lib.Repository;

/**
 * Simple service to make git related tasks easier.  
 *
 */
public interface GitService {

	/**
	 * Clone the git repository given by {@code cloneUrl} only if is does not exist yet in {@code folder}.
	 * 
	 * @param folder The folder to store the local repo.
	 * @param cloneUrl The repository URL.
	 * @return The repository object (JGit library).
	 * @throws Exception propagated from JGit library.
	 */
	Repository cloneIfNotExists(String folder, String cloneUrl/*, String branch*/) throws Exception;
	
	Repository openRepository(String folder) throws Exception;

	int countCommits(Repository repository, String branch) throws Exception;
	
	void checkout(Repository repository, String commitId) throws Exception;
}
