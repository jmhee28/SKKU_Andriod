# SKKU_Andriod

## IDEA
Android studio

## 1.Initial Screen
If you start the application the login page will show up.

### Click "로그인" button 
1. Check whether the ID and it's password in AWS RDS.
2. If it is not contained in AWS RDS, show Toast message "로그인 실패"
3. Else if it is contained in AWS RDS, convert the Activity as below image.

### Click "회원가입" button
Type user's imformations, then the informations will be added into AWS RDS.

<img src= "https://user-images.githubusercontent.com/92200502/173092669-9f7e601d-ec27-4009-8836-574db8e52f66.jpg" width="200" height="400"/>

## 2.Current
It shows current temperature and the recommended fashion. The fasion is determined by the temperature and it's data is located in AWS S3 bucket.

### Click "주간 날씨"
Switch the Activity as below image

<img src= "https://user-images.githubusercontent.com/92200502/173094167-1206bad0-c7cd-494c-90a0-3e3fb1bf5904.jpg" width="200" height="400"/>

## 3.Week
Shows climate of five days

<img src= "https://user-images.githubusercontent.com/92200502/173092682-744e8603-7ddb-4040-b6d6-e673d6c00aa0.jpg" width="200" height="400"/>
