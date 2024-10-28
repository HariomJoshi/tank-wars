## Contributing

Please follow these steps to ensure a smooth contribution process:

1. **Fork the Repository**  
   Start by forking the parent repository to create your own copy.

2. **Clone Your Fork Locally**  
   Set up your local repository by cloning your forked version of the repository:

   ```bash
   git clone <your-fork-url>
   ```

3. **Set Upstream Remote**  
   Link the original repository to your local repository to easily sync changes:

   ```bash
   git remote add upstream <parent-repo-url>
   ```

4. **Sync with the Parent Repository**  
   Always ensure that your fork is up-to-date with the latest changes from the main repository:

   ```bash
   git fetch upstream
   ```

5. **Pull Changes to Your Local Repository**  
   Before starting any new functionality, pull the latest changes from the parent repository:

   ```bash
   git pull upstream main
   ```

6. **Create a New Branch**  
   For each new functionality or fix, create a separate branch:

   ```bash
   git checkout -b <branch-name>
   ```

7. **Develop and Commit**  
   Implement your functionality. When done, commit your changes with a meaningful message describing the work:

   ```bash
   git commit -m "Add meaningful message"
   ```

8. **Sync with Parent Repository Again**  
   Before pushing, re-sync with the main repository to ensure no updates have been missed:

   ```bash
   git fetch upstream
   ```

9. **Pull Changes to Local**  
   Apply any new changes from the parent repository to your branch:

   ```bash
   git pull upstream main
   ```

10. **Resolve Merge Conflicts (if any)**  
    If there are conflicts, resolve them locally.

11. **Push to Your Forked Repository**  
    Push your branch with the new changes back to your forked repository:

    ```bash
    git push origin <branch-name>
    ```

12. **Create a Pull Request**  
    Navigate to the parent repository and create a pull request from your fork, describing your changes and the purpose of the PR.

Thank you for your contributions! Please reach out if you have any questions.
