![](https://git-scm.com/book/en/v2/images/lifecycle.png)

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

