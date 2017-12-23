#!/bin/bash

#echo Modified files:
#git ls-files -m
#echo
#echo Untracked files:
#git ls-files --others --exclude-standard
#echo
#echo Deleted files:
#git ls-files -d


#git config credential.helper store
#git push https://rcantrel:MyHonor7@github.com/rcantrel/eclipse.git
#Username for 'https://github.com': <USERNAME>
#Password for 'https://USERNAME@github.com': <PASSWORD>
#git config --global credential.helper 'cache --timeout 21600'


commitAndPush() {
  echo -- Commiting
  git commit -m "update"
  echo -- Pushing
  git push https://rcantrel:MyHonor7@github.com/rcantrel/JCommunique2.git
}

shouldCommitPush=false
echo
echo ---------------------------------------------
echo -- Add Non executable modified files
echo ---------------------------------------------
files=($(git ls-files -m))
for i in "${files[@]}"
do
  if [[ "$i" != *"/executables/"* ]]
    then
	  git add $i
	  addedText="  Added $i"
	  echo $addedText
	  shouldCommitPush=true
  fi
done

echo ---------------------------------------------
echo -- Add Non executable untracked files
echo ---------------------------------------------
files=($(git ls-files --others --exclude-standard))
for i in "${files[@]}"
do
  if [[ "$i" != *"/executables/"* ]]
    then
	  git add $i
	  addedText="  Added $i"
	  echo $addedText
	  shouldCommitPush=true
  fi
done

echo ---------------------------------------------
echo -- Remove deleted files
echo ---------------------------------------------
files=($(git ls-files -d))
for i in "${files[@]}"
do
  git rm $i
  addedText="  Removed $i"
  echo $addedText
  shouldCommitPush=true
done


if [ "$shouldCommitPush" = true ]
  then
    echo
	echo Commit and Push changes
    commitAndPush
  else
    echo
	echo Nothing to commit
fi

#-----------------------------------------------------------------

echo
echo ---------------------------------------------
echo -- Add Commit and Push executable modified files
echo ---------------------------------------------
files=($(git ls-files -m))
for i in "${files[@]}"
do
  if [[ "$i" == *"/executables/"* ]]
    then
	  FILENAME=$PWD"/"$i
      FILESIZE=$(stat -c%s "$FILENAME")
	  if [ "$FILESIZE" -lt "100000000" ];then
		git add $i
	    addedText="  Added $i"
	    echo $addedText
	    commitAndPush
	  else
		echo "Size of $FILENAME = $FILESIZE bytes. Which is to big for github"
      fi  
	  echo
  fi
done

#-----------------------------------------------------------------

echo
echo ---------------------------------------------
echo -- Add Commit and Push executable untracked files
echo ---------------------------------------------
files=($(git ls-files --others --exclude-standard))
for i in "${files[@]}"
do
  if [[ "$i" == *"/executables/"* ]]
    then
	  FILENAME=$PWD"/"$i
      FILESIZE=$(stat -c%s "$FILENAME")
	  if [ "$FILESIZE" -lt "100000000" ];then
		git add $i
	    addedText="  Added $i"
	    echo $addedText
	    commitAndPush
	  else
		echo "Size of $FILENAME = $FILESIZE bytes. Which is to big for github"
      fi  
	  
	  echo
  fi
done

#-----------------------------------------------------------------

echo
echo
git status
echo

echo ---------------------------------------------
echo -- Script complete
echo ---------------------------------------------