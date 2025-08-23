The `develop` branch is for **the entire team (Frontend, Backend, DevOps, etc.)**.
It is the common integration branch where all code is merged and tested together.

---

### Flow in a Monorepo:

Imagine that your monorepo has this structure:
```
monorepo/
├── apps/
│   ├── frontend-web/   (Equipo Frontend)
│   └── backend-api/    (Equipo Backend)
├── libs/
│   ├── shared-ui/      (Puede ser usado por ambos)
│   └── shared-utils/   (Puede ser usado por ambos)
```

This is how `develop` would work:

1.  **Frontend developers** create their `feature/frontend-new-page` branches from `develop`.
2.  **Backend developers** create their `feature/backend-new-endpoint` branches from `develop`.
3.  Both merge their feature branches into `develop`.
4.  **`develop` becomes the “thermometer” for the entire project.** Here you can see if the new frontend is compatible with the new backend, if the shared libraries don't break anything, etc.


### Why is this better than having separate branches?

* **Detect conflicts and incompatibilities early:** If the API changes and the frontend doesn't adapt, it's discovered in `develop`, not in production (`main`).
* **Continuous integration:** Facilitates the implementation of CI pipelines (GitHub Actions, GitLab CI) that test **the entire system together** every time `develop` is updated.
* **Simplified coordination:** The entire team shares a single status of “what is being developed.” A `git pull origin develop` gives you the latest version of the entire project.


---

### Summary of branches and their use for teams:

| Branch          | Purpose                                                                     | Who uses it                                                                  |
| --------------- | --------------------------------------------------------------------------- | ---------------------------------------------------------------------------- |
| **`main`**      | **Stable production.**                                                      | The entire team (for deployments and hotfixes).                              |
| **`develop`**   | **Integration.** The most advanced version with the new features completed. | **The entire team.** It is the basis for creating new features.              |
| **`feature/*`** | **Development of a new feature.**                                           | **Specific teams** (e.g., `feature/login-frontend`, `feature/auth-backend`). |
| **`release/*`** | **Prepare a new version for production.**                                   | Release Manager / DevOps.                                                    |
| **`hotfix/*`**  | **Fix critical bugs in production.**                                        | The person assigned to the urgent fix.                                       |


**Conclusion:** Create a single `develop` branch that serves as a common trunk for everyone.
 This is the most efficient way to ensure that all parts of your monorepo evolve and are tested in an integrated manner.


---

## Create develop Branch

```
$ git brach develop
```


## Switch to develop Branch

```
$ git switch develop
```

---

## Create feature Branch

You should create `feature/*` branches from `develop` because:

1.  **You ensure the correct base:** Your new feature will start from the most up-to-date code, which includes all the latest changes from your teammates.
2.  **You avoid contamination:** If you are on `main` (production) and create a feature branch, you might base your work on code that isn't ready for integration, leading to potential conflicts or instability.
3.  **You maintain consistency:** This is a fundamental rule of the GitFlow workflow. All new features are branched from `develop`.


**Safe command sequence:**
```
# 1. Always switch to develop and get the latest changes
git switch develop
git pull

# 2. Now, create and switch to the new feature branch
git branch feature/new-feature
```

---

#### Create branch feature/login-frontend
    git branch feature/login-frontend

#### Create branch feature/auth-backend
    git branch feature/auth-backend


#### Create branch hotfix/ (for urgent fixes)
    git branch hotfix/critical-bug-fix


---

#### See which branch you are in
    git branch

#### View all branches (local and remote)
    git branch -a

#### View history in one line.
    git log --oneline

#### View branch history graph
    git log --oneline --graph --all


#### eg:
```
➜  RecruitingApp git:(develop) ✗ git branch -a
* develop
  feature/auth-backend
  feature/login-frontend
  hotfix/critical-bug-fix
  main
  remotes/origin/HEAD -> origin/main
  remotes/origin/main
```

---

`git pull` and `git push` without arguments only work with the **current branch**.

---

## ✅👷To upload all your local branches to the remote repository (GitHub), you need to do it branch by branch.

### 1. First, push the `develop` branch:
```
    git switch develop
    git push -u origin develop
```

**💡Note:** The `-u` flag is only necessary the first time to establish the tracking relationship.
 💡After this, you can simply use `git push` and `git pull` from each branch.

```
man 1 git-push
-u, --set-upstream
            For every branch that is up to date or successfully pushed, add
            upstream (tracking) reference, used by argument-less git-pull(1) and
            other commands. For more information, see branch.<name>.merge in
            git-config(1).
```


### 2. Then, push the feature branches:
```
    git switch feature/login-frontend
    git push -u origin feature/login-frontend
    
    git switch feature/auth-backend
    git push -u origin feature/auth-backend
```

### 3. Finally, push the hotfix branch:
```
    git switch hotfix/critical-bug-fix
    git push -u origin hotfix/critical-bug-fix
```

---

## ✅🚀Faster alternative command (from any branch):
```
    # You can push specific branches without switching to them
    git push origin develop
    git push origin feature/login-frontend
    git push origin feature/auth-backend
    git push origin hotfix/critical-bug-fix
```

## After pushing all, verify with:
```
    git branch -a
```

You should see something like:
```
    * main
      develop
      feature/auth-backend
      feature/login-frontend
      hotfix/critical-bug-fix
      remotes/origin/main
      remotes/origin/develop
      remotes/origin/feature/login-frontend
      remotes/origin/feature/auth-backend
      remotes/origin/hotfix/critical-bug-fix
```
