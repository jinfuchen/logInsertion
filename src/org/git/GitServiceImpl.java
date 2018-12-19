package org.git;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.RevWalkUtils;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.git.GitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitServiceImpl implements GitService {

	private static final String REMOTE_REFS_PREFIX = "refs/remotes/origin/";
	Logger logger = LoggerFactory.getLogger(GitServiceImpl.class);
	
	@Override
	public Repository cloneIfNotExists(String projectPath, String cloneUrl/*, String branch*/) throws Exception {
		File folder = new File(projectPath);
		Repository repository;
		if (folder.exists()) {
			RepositoryBuilder builder = new RepositoryBuilder();
			repository = builder
					.setGitDir(new File(folder, ".git"))
					.readEnvironment()
					.findGitDir()
					.build();
			//logger.info("Project {} is already cloned, current branch is {}", cloneUrl, repository.getBranch());
		} else {
			logger.info("Cloning {} ...", cloneUrl);
			Git git = Git.cloneRepository()
					.setDirectory(folder)
					.setURI(cloneUrl)
					.setCloneAllBranches(true)
					.call();
			repository = git.getRepository();
			//logger.info("Done cloning {}, current branch is {}", cloneUrl, repository.getBranch());
		}
		return repository;
	}
	@Override
	public Repository openRepository(String repositoryPath) throws Exception {
	    File folder = new File(repositoryPath);
	    Repository repository;
	    if (folder.exists()) {
	        RepositoryBuilder builder = new RepositoryBuilder();
	        repository = builder
	            .setGitDir(new File(folder, ".git"))
	            .readEnvironment()
	            .findGitDir()
	            .build();
	    } else {
	        throw new FileNotFoundException(repositoryPath);
	    }
	    return repository;
	}

	public void checkout(Repository repository, String commitId) throws Exception {
	    logger.info("Checking out {} {} ...", repository.getDirectory().getParent().toString(), commitId);
	    try (Git git = new Git(repository)) {
	        CheckoutCommand checkout = git.checkout().setName(commitId);
	        checkout.call();
	    }
	}


	@Override
	public int countCommits(Repository repository, String branch) throws Exception {
		RevWalk walk = new RevWalk(repository);
		try {
			Ref ref = repository.findRef(REMOTE_REFS_PREFIX + branch);
			ObjectId objectId = ref.getObjectId();
			RevCommit start = walk.parseCommit(objectId);
			walk.setRevFilter(RevFilter.NO_MERGES);
			return RevWalkUtils.count(walk, start, null);
		} finally {
			walk.dispose();
		}
	}
}
