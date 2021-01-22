# Local

<img src="https://git-scm.com/book/en/v2/images/lifecycle.png" style="zoom: 67%;" />

## add

：将内容添加到下一次提交中

1. 开始跟踪新文件 untracked->staged
2. 把已跟踪的文件放到暂存区 modified->staged
3. 合并时把有冲突的文件标记为已解决状态 

git diff ：当前文件和暂存区域快照之间的差异（当前文件与上次add的差异）

git diff --staged（--cached）：已暂存文件与上一次提交的文件差异（上次add与上次commit的差异）

## commit

：提交所有暂存区（因add而staged）的快照

-m：填写的**提交信息**

-a：跳过使用暂存区，自动把所有已经跟踪过的文件(新add的文件、新modified的文件)暂存起来一并提交。（新创建尚未(add)untracked的文件不在暂存区）

git rm：从暂存区移除，且以后不再跟踪某文件（从上次add中移除，以后也不再记录）

git log git status

git commit --amend：修改并覆盖上次commit。（添加几个漏掉的文件，或重新填写**提交信息**）

### 原理

1. 当`git commit` 时，Git 会先计算每一个子目录的校验和， 并把这些校验和保存为树对象。
2. 然后Git 便会创建一个提交对象

仓库中产生多个文件快照***blob*** 对象、一个 **树** 对象 （记录着目录结构和 blob 对象索引）、一个 **提交** 对象（包含着指向树对象的指针、指向上次提交的指针、所有提交信息）

<img src="https://git-scm.com/book/en/v2/images/commit-and-tree.png" style="zoom:50%;" />

做些修改后再次提交，那么这次产生的提交对象会包含一个指向上次提交对象（父对象）的指针

<img src="https://git-scm.com/book/en/v2/images/commits-and-parents.png" style="zoom:50%;" />



## branch

Git 分支，其本质上是**指向提交对象的可变指针**，并随新提交对象移动

 `HEAD` 是特殊指针，指向当前所在的本地分支

 `git branch <newbranchname>` 命令仅仅 **创建** 一个新分支，并不会自动切换到新分支中去。`HEAD` 指向不变

 `git checkout <branchname>` 命令，切换到一个已存在的分支。 `HEAD` 指向改变

用 `git checkout -b <newbranchname>` 创建一个新分支后立即切换过去

`git log` 命令查看各个分支当前所指的提交对象 `--oneline` `--decorate`

`git branch -d`删除分支

`git branch -v` 命令查看每一个分支的最后一次提交

 `git branch --merged`与 `--no-merged`查看哪些分支是否已经合并到当前分支

## tag

 `git tag` 列出tag `git tag -l "v1.8.5*"`列出符合此名称的tag

使用 `git show` 命令可以看到标签信息和与之对应的提交信息

**创建方式**

1. `git tag -a v1.4 -m "my version 1.4"`创建v1.4的tag，信息为my version 1.4。`-m` 输入提交信息，如同commit，不写-m 会弹出输入。
2. `git tag v1.4` 不使用 `-a`、`-s` 或 `-m` 选项。创建轻标签，`git show` 无提交信息

**后期打标签**

```shell
$ git log --pretty=oneline
15027957951b64cf874c3557a0f3547bd83b3ff6 Merge branch 'experiment'
a6b4c97498bd301d84096da251c98a07c7723e65 beginning write support
9fceb02d0ae598e95dc970b74767f19372d61af8 updated rakefile
$ git tag -a v1.2 9fceb02
```

**推送标签到远程**

 `git push origin <tagname>` 推送一个

`git push origin --tags`推送全部

**删除标签**

`git tag -d <tagname>`

`git push origin --delete <tagname>`删除远程

**检出标签并创建分支**

单检出标签，会造成离头指针（detached HEAD）的状态，所以通常在检出标签时创建一个新分支

```shell
git checkout -b version2 v2.0.0
Switched to a new branch 'version2'
```

## merge/rebase

<img src="https://git-scm.com/book/en/v2/images/basic-rebase-1.png" alt="分叉的提交历史。" style="zoom:50%;" />

### `merge` 命令：

会把两个分支的最新快照（`C3` 和 `C4`）以及二者最近的共同祖先（`C2`）进行三方合并，并将合并结果生成一个新的快照（并提交）。

有合并冲突时`git status` 命令来查看，处于unmerged状态。需修改冲突后，add重新跟踪

<img src="https://git-scm.com/book/en/v2/images/basic-rebase-2.png" style="zoom:50%;" />

### `rebase` 命令：

提取 `C4` 中的修改变基到 `C3` 上。提取分支中新增的修改，然后把分支的起点提到主分支的最新位置，然后合并。

原理:

1. 找到这两个分支最近共同祖先 
2. 把当前分支相对于该祖先的历次提交提取为临时文件
3. 移动当前分支指向，指向基底分支的最新位置，然后合并

1.检出 `experiment` 分支，然后将它变基到 `master` 分支上

```shell
$ git checkout experiment
$ git rebase master
```

<img src="https://git-scm.com/book/en/v2/images/basic-rebase-3.png" style="zoom:50%;" />
2.回到 master 分支，进行一次快进合并。

```shell
$ git checkout master
$ git merge experiment
```

<img src="https://git-scm.com/book/en/v2/images/basic-rebase-4.png" style="zoom:50%;" />

`git checkout server` `git rebase master ` 切换到server,将当前分支变基到`master`上。

`git rebase <basebranch> <topicbranch>`

`git rebase master server` 将主题分支  `server`变基到`master`上。不用先切换到server分支

### 高级rebase

server是master的子分支，client是server的子分支

<img src="https://git-scm.com/book/en/v2/images/interesting-rebase-1.png" style="zoom:50%;" />

1、选中在 `client` 分支里但不在 `server` 分支里的修改（即 `C8` 和 `C9`），将它们在 `master` 分支上变基

```shell
$ git rebase --onto master server client
```

<img src="https://git-scm.com/book/en/v2/images/interesting-rebase-2.png" style="zoom:50%;" />

2、合并`client`到 `master` 分支

```shell
$ git checkout master
$ git merge client
```

<img src="https://git-scm.com/book/en/v2/images/interesting-rebase-3.png" style="zoom:50%;" />

3、将分支 `server`变基到`master`分支上

```shell
$ git rebase master server
```

<img src="https://git-scm.com/book/en/v2/images/interesting-rebase-4.png" style="zoom: 67%;" />

4、合并`server`到 `master` 

```shell
$ git checkout master
$ git merge server
//删除没用的分支
$ git branch -d client
$ git branch -d server
```

<img src="https://git-scm.com/book/en/v2/images/interesting-rebase-5.png" style="zoom: 67%;" />

## cherry-pick

举例来说，代码仓库有`master`和`feature`两个分支。

> ```bash
>  a - b - c - d   Master
>       \
>         e - f - g Feature
> ```

现在将提交`f`应用到`master`分支。

> ```bash
> # 切换到 master 分支
> $ git checkout master
> 
> # Cherry pick 操作
> $ git cherry-pick f
> ```

上面的操作完成以后，代码库就变成了下面的样子。

> ```bash
>  a - b - c - d - f   Master
>       \
>         e - f - g Feature
> ```

从上面可以看到，`master`分支的末尾增加了一个提交`f`。

`git cherry-pick <commitHash>`将指定的提交（commit）应用于其他分支(`git cherry-pick feature`将 `feature`分支的最近一次提交，转移到当前分支。)

`git cherry-pick <HashA> <HashB> ...`将 A 和 B 两个提交应用到当前分支。会在当前分支生成两个对应的新提交。

`git cherry-pick A..B `从A到B应用到当前分支

`git cherry-pick A^..B `从A到B应用到当前分支（不包括A）

`-x`**可选参数**附带原信息

**冲突**

和merge/rebase一样，发生代码冲突，Cherry pick 会停下来，让用户决定如何继续操作

修改完成后`git add .`  然后`git cherry-pick --continue`继续

`git cherry-pick --abort`放弃合并恢复到冲突前



# remote

## 远程仓库origin

`git remote`列出远程仓库  `-v` 会显示远程仓库URL其对应的名称

`git remote add <shortname> <url>` 添加远程仓库，并命名，以后可用该名称代指远程仓库。

首次与远程仓库交互

- 用 `clone` 命令克隆了一个仓库。会自动将其添加为远程仓库并默认以 “origin”命名。并自动设置本地 master 分支跟踪克隆的远程仓库的 `master` 分支（或其它名字的默认分支）
- 或`init`一个本地仓库，然后`git remote add <shortname> <url>`添加远程仓库，然后获取或推送

`git remote show <remote>`查看远程仓库信息，如origin

```
$ git remote show origin
* remote origin //当前远程仓库
  URL: https://github.com/my-org/complex-project
  Fetch URL: https://github.com/my-org/complex-project
  Push  URL: https://github.com/my-org/complex-project
  HEAD branch: master //当前本地分支
  Remote branches://远程分支的跟踪情况
    master                           tracked
    dev-branch                       tracked
    markdown-strip                   tracked
    issue-43                         new (next fetch will store in remotes/origin)
    issue-45                         new (next fetch will store in remotes/origin)
    refs/remotes/origin/issue-11     stale (use 'git remote prune' to remove)
  Local branches configured for 'git pull'://在特定本地分支执行pull时，对应的远程分支
    dev-branch merges with remote dev-branch
    master     merges with remote master
  Local refs configured for 'git push'://在特定本地分支执行push时，对应的远程分支
    dev-branch                     pushes to dev-branch                     (up to date)
    markdown-strip                 pushes to markdown-strip                 (up to date)
    master                         pushes to master                         (up to date)
```

`git remote remove <remote>`   删除远程仓库

`git remote remove <remote> <newname>` 重命名远程仓库

## 远程分支master

远程分支是一种远程引用，是对远程仓库的分支/标签引用（指针），

`git ls-remote <remote>` 获得远程引用的完整列表 （或`git remote show <remote>`远程仓库信息）

远程分支：以 `<remote>/<branch>` 的形式命名（如 `origin/master` ）。本地不从 `origin` 远程仓库`fetch`\`pull`，本地的 `origin/master` 指针就不会移动；一旦从远程仓库拉取，`origin/master` 指针便反映其最新状态。

本地支必须显式地推送到远程仓库，否则不会自动与其同步。

1. LocalA:`git push <remote> <branch>`（`git push origin serverfix`）：推送本地的 `serverfix` 分支来更新远程仓库上的 `serverfix` 分支。
2. LocalB:当抓取到新的远程跟踪分支时，本地只有一个`origin/serverfix` 指针。a、可以 `git merge origin/serverfix` 合并到当前所在的分支。或b、`git checkout -b serverfix origin/serverfix`建立一个起点位于 `origin/serverfix`的本地跟踪分支

 **跟踪分支**：在一个跟踪分支上`fetch` `pull` ` push` 能自动地识别去哪个远程仓库上抓取、合并到哪个本地分支

- b（`git checkout -b <branch> <remote>/<branch>`）从一个远程分支检出一个本地分支，会自动创建所谓的“跟踪分支”。
  `git checkout -b <branch> <remote>/<branch>` 缩写`git checkout --track origin/serverfix` 

  再缩写`git checkout serverfix`检出的分支本地不存在，但原创仓库存在时，会自动创建本地跟踪分支

  最简缩写最常用，全写通常用作检出并改名时

- 克隆一个仓库时，它通常会自动地创建一个跟踪 `origin/master` 的 `master` 分支

- 为已有的本地分支手动跟踪一个刚刚拉取下来的远程分支，使用 `git branch -u <remote>/<branch>`  `(--set-upstream-to)` 

`git branch -vv`:查看所有跟踪分支的领先落后信息，对比的是从远程仓库最后一次抓取的数据，未必是最新的（`git fetch --all; git branch -vv`先抓取所有的远程仓库再比较）

`git push origin --delete serverfix`删除一个远程分支


 `git fetch` 命令只会将数据下载到你的本地仓库

`git pull`命令自动抓取后合并该远程分支到当前分支 （fetch+marge）

### 远程rebase

**不要对已经推送至共用仓库的提交上执行变基命令**

若一定做，请一定要通知每个人执行 `git pull --rebase` 命令

#### 错误演示

1、原始状态

<img src="https://git-scm.com/book/en/v2/images/perils-of-rebasing-1.png" alt="原始状态" style="zoom:50%;" />

2、你抓取了这些在远程分支上的修改，并将其合并到你本地的开发分支。

<img src="https://git-scm.com/book/en/v2/images/perils-of-rebasing-2.png" style="zoom:50%;" />

3、其他人把合并操作回滚，改用变基；继而又用 `git push --force` 命令覆盖了服务器上的提交历史

<img src="https://git-scm.com/book/en/v2/images/perils-of-rebasing-3.png" style="zoom:50%;" />

4、再次拉去时，远程仓库已改变。问题出现：再次合并 `C4` 和 `C6`又出现在历史中

<img src="https://git-scm.com/book/en/v2/images/perils-of-rebasing-4.png" style="zoom:50%;" />

#### 解决

 `git rebase teamone/master`

- 检查哪些提交是我们的分支上独有的（C2，C3，C4，C6，C7）
- 检查其中哪些提交不是合并操作的结果（C2，C3，C4）
- 检查哪些提交在对方覆盖更新时并没有被纳入目标分支（只有 C2 和 C3，因为 C4 其实就是 C4'）
- 把查到的这些提交应用在 `teamone/master` 上面

简单的方法是使用 `git pull --rebase` 命令而不是直接 `git pull`（先 `git fetch`，再 `git rebase teamone/master`）

<img src="https://git-scm.com/book/en/v2/images/perils-of-rebasing-5.png" style="zoom:50%;" />
