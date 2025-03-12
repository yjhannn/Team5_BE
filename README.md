# Ojosama

![image](https://github.com/user-attachments/assets/6c8c813e-f182-4a13-bc21-cf1a7a812382)

## 테스트용 계정
- 이메일: ojosama55@gmail.com
- 비밀번호: kakao2024

## 목차

- [🍪노션 페이지](#-https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a)
- [✨프로젝트 소개](#-https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=5f78b4ccb12246eca8f41904e9fce4d6&pm=s)
- [🙋🏻‍♂️내가 기여한 부분]
- [🖥️배포 링크](#http://talkak-fe.s3-website.ap-northeast-2.amazonaws.com)
- [🤝그라운드 룰](#-https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=93d3274c7a8b4d159b38b16115c3ff42&pm=s)
- [📜커밋 컨벤션](#-https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=17afc129cb144bfea2fc3cea94ea153d&pm=s)
- [👨‍💻코딩 컨벤션](#-https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=833e57b063094194b6150558d89af6ec&pm=s)
- [📋API 명세서](#-https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=c1164ec45e4a485284644935bdb9a29f&pm=s)
- [❎에러코드 정의서](#-https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=de999aae2f53421088e7edd875702089&pm=s)
- [🛠️테스트 결과 보고서](#-https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=1243d1ecfbac437abbffece87f8f7c8a&pm=s)

## ✨ 프로젝트 소개

영상 하이라이트를 분석하여 쇼츠를 생성해주는 쇼츠 자동화 서비스

## 프로젝트 설명

# 서비스 개요

**딸깍**은 크리에이터들이 손쉽게 긴 영상에서 핵심 장면을 자동으로 추출하고, 짧고 임팩트 있는 숏폼 콘텐츠로 편집해주는 AI 기반 서비스입니다.
사용자는 **딸깍**을 통해 클릭 한 번으로 하이라이트 영상을 생성할 수 있을 뿐 아니라, 사용자 간 피드백을 주고받으며 성장할 수 있는 커뮤니티도 제공합니다.

# 서비스 기획 의도

최근 영상 소비 트렌드는 짧고 핵심적인 **숏폼** 위주로 변하고 있습니다.
이러한 흐름에 맞춰, 딸깍은 영상 편집의 진입 장벽을 낮춰 누구나 손쉽게 숏폼 영상을 만들고 공유할 수 있도록 돕는 것을 목표로 하고 있습니다.
또한, 서로의 영상을 시청하고 피드백을 교환할 수 있는 커뮤니티를 통해 사용자들이 상호작용하며 함께 성장할 수 있는 플랫폼을 제공합니다.

# 주요 기능

**1. 하이라이트 추출 및 숏폼 생성**: AI가 긴 영상에서 주요 장면을 분석하여 자동으로 숏폼 영상으로 편집해주어, 짧은 시간 내에 완성도 높은 콘텐츠를 제작할 수 있습니다.

**2. 커뮤니티 기반 피드백**: 사용자 간에 서로의 콘텐츠에 대해 피드백을 교환하고, 이를 바탕으로 콘텐츠를 개선할 수 있는 커뮤니티 기능을 제공합니다.

**3. 사용자별 추천 영상**: 각 사용자의 관심사와 영상을 분석하여, 개인에게 적합한 흥미로운 콘텐츠를 추천합니다.

# 아키텍쳐
![기술스택](https://github.com/user-attachments/assets/49ab3399-90a7-49c8-9c59-1499948adda6)

# 내가 기여한 부분
## 기능 구현
- 레퍼런스 기능 구현
- 영상 추천 알고리즘 메서드 작성

## 성능 향상
- 캐싱 처리
- 좋아요 및 조회수 Mysql에서 Redis로 이전

## 테스트
- 데모데이터 추가
- 테스트 코드

# 프로젝트 소감
- 서비스 개발이라는 것은 정말 많은 것을 고려해야 한다. 서비스를 쓰는 고객, 관리자, 기능, 비용 등등... 개발을 진행하면서 API 호출량 제어, 빠른 데이터 처리를 고민하며 기능을 구현해보며 redis, ehcahce 등 다양한 기술들이 있다는 것을 느꼈다. 개발자는 이와 같은 다양한 기술들을 어떻게 학습하고 적용하는지가 정말 중요한 능력이라는 것을 체감할 수 있었다. 그리고 팀프로젝트는 다들 하는 말이지만 소통이 결국 제일 중요하다. 어려운 부분을 공유해야 해결이 빠르다.
- 팀원들이 진행한 Spring Security, AI 활용에 대해서도 같이 찾아보며 배울 수 있는 점이 많았다. 비록 당시에 큰 도움은 안됐던 것 같지만 그러한 경험을 통해 다양한 지식을 얻을 수 있었다.
- CI/CD와 배포의 중요성 및 어려움. 그리고 도커를 얼른 배워야겠다. 

# 기술적 시도

## Spring Security

- Youtube Shorts와 접목시킨 서비스로서 사용자 경험을 향상시키기 위해 Google OAuth 2.0 로그인을 도입하였습니다. 
- Step2에서는 OAuth 2.0을 직접 구현했으나, Step3에서는 Spring Security 프레임워크를 사용해 구현을 개선하였습니다. 
- 현재 도메인을 통일할 수 없는 환경으로 인해 로그인 후 발급되는 토큰을 쿠키나 세션으로 전달하기 어려웠지만, 보안을 강화하기 위해 OAuth 2.0 인증 프로세스를 변형하고 Spring Security 설정을 커스터마이징하여 최소한의 보안을 확보했습니다.

## Redis

- 영상 추천 알고리즘과 좋아요 기능을 구현하기 위해 정보 읽기, 쓰기가 빠른 Redis 저장소를 사용하였습니다.
- 추천 알고리즘의 경우 유저의 추천 영상을 생성하기 위한 메타데이터(영상 시청 정보, 좋아요 정보, 선호 카테고리 정보)를 기반으로 추천 영상 리스트를 생성하게 하였고, 좋아요 기능은 Redis 동시성 제어 기능을 활용하여 안전하게 전체 좋아요 수가 반영되도록 설계하였습니다. 
- Docker 컨테이너로 서버에 배포하여 배포의 안정성을 높였고, 테스트용 Redis 컨테이너를 별도로 구성하여 테스트에도 활용될 수 있도록 하였습니다.

## Ehcache

- 저희 서비스에서는 많은 양의 외부 API 호출을 효율적으로 처리하기 위해 스프링 내장 캐시인 Ehcache를 도입하여 캐싱 전략을 구현하였습니다. 
- 인기 있는 유튜브 쇼츠와 카테고리별 쇼츠 영상을 가져올 때, 6시간 간격으로 캐시를 업데이트하여 최신 데이터를 제공하도록 구현했습니다. 
- 이를 통해 유튜브 쇼츠 레퍼런스 기능에서 자주 요청되는 데이터를 캐싱하여 응답 속도를 개선하고 네트워크 비용을 절감하는데 주력했습니다.

## OpenAI + FastAPI

> https://github.com/Dockerel/highlight-extractor
### 데이터 흐름 상세 설명

1.	사용자 요청
    클라이언트에서 Google 계정으로 로그인한 사용자가 YouTube URL, 제목, 카테고리 등의 정보를 입력합니다.
    해당 정보는 Spring Framework 웹 애플리케이션에서 FastAPI 애플리케이션의 /extract-highlights 엔드포인트로 전송됩니다.
2.	비디오 하이라이트 추출 요청 (FastAPI)
    FastAPI는 요청을 수신한 후, 해당 YouTube 영상을 다운로드하고 사이즈를 조정합니다.
    Whisper 모델을 이용하여 비디오의 스크립트를 추출하고, GPT 모델을 통해 하이라이트 구간을 탐색합니다.
    이 과정은 비동기적으로 처리되며, FastAPI는 이 요청에 대해 task_id를 응답으로 반환합니다. task_id는 FastAPI의 로컬 스토리지에서 비동기 작업 상태와 결과 데이터를 관리하는 키로 사용됩니다.
3.	작업 상태 조회 (Polling)
    클라이언트는 task_id를 이용해 FastAPI의 /task-status/{task_id} 엔드포인트에 polling을 시도하며 작업 상태를 확인합니다.
    작업 상태가 “완료”로 변경되면 클라이언트는 FastAPI의 /select-highlight/{task_id} 엔드포인트를 호출하여, 생성된 5개의 하이라이트 후보 영상의 S3 URL과 관련 메타데이터(DTO)를 받아옵니다.
4.	하이라이트 선택 및 업로드
    사용자는 5개의 하이라이트 중 하나를 선택하고, 프론트엔드에서 FastAPI의 /select-highlight 엔드포인트로 task_id와 선택한 영상의 인덱스(index)를 전송합니다.
    FastAPI는 선택한 하이라이트를 Spring Framework 웹 애플리케이션의 /api/videos/create 엔드포인트에 전달하여, 최종적으로 해당 하이라이트를 Video 객체로 웹 서비스에 업로드합니다.
5.	원본 비디오 다운로드 
    업로드된 비디오가 Video 객체로 생성된 후, 사용자는 원본 비디오를 다운로드할 수 있습니다.
    클라이언트는 Spring 웹 애플리케이션의 /api/videos/{videoId}/extract 엔드포인트에 요청하여, 원본 비디오의 AWS S3에 저장된 presigned URL을 받아 로컬로 다운로드할 수 있습니다.
```    
      ┌────────────┐
      │   Client   │
      └─────┬──────┘
      │
      (User Input: │ YouTube URL, ...)
      │
      ▼
      ┌─────────────────────────────┐
      │ Spring Framework Web Server │
      └─────────────┬───────────────┘
      │
      POST /extract-highlights  (video info)
      │
      ▼
      ┌───────────────────┐
      │   FastAPI Server  │
      └───────┬───────────┘
      │
      ┌──────────────▼───────────────┐
      │ Background Task (Async)      │
      │ - Video download             │
      │ - Resize, Whisper model      │
      │ - Highlight extraction (GPT) │
      └──────────────┬───────────────┘
      │
      Respond with │ task_id
      │
      ▼
      ┌────────────────────┐
      │   Client (Polling) │
      └─────────▲──────────┘
      │
      GET /task-status/{task_id}
      │
      If Status = "Complete"
      │
      ▼
      GET /select-highlight/{task_id}
      │
      │
      (5 highlight │ S3 URLs)
      │
      ▼
      ┌────────────────────────────┐
      │ Client selects one video   │
      │ POST /select-highlight     │
      └──────────┬─────────────────┘
      │
      │
      ▼
      ┌───────────────────────────────┐
      │ Spring Framework Web Server   │
      │ POST /api/videos/create       │
      └───────────────────────────────┘
      │
      │
      ▼
      ┌────────────────────┐
      │   AWS S3 Storage   │
      └────────────────────┘
      │
      │
      GET /api/videos/{videoId}/extract
      │
      ▼
      Client receives presigned URL
      (Download)
```      
## ERD 다이어그램

<img width="1053" alt="스크린샷 2024-11-15 오전 3 28 42" src="https://github.com/user-attachments/assets/20f07d26-fa07-4232-b099-b94441f2eb9f">

### 팀명

> **오조사마**

### 팀구성원 소개

| [<img src="https://avatars.githubusercontent.com/u/62774721?v=4" width="100px">](https://github.com/anaconda77) | [<img src="https://avatars.githubusercontent.com/u/74302278?v=4" width="100px">](https://github.com/Dockerel) | [<img src="https://avatars.githubusercontent.com/u/114137788?v=4" width="100px">](https://github.com/hyeywon) |
|:---------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------:|
|                                   [경북대 BE 신성민](https://github.com/anaconda77)                                   |                                   [경북대 BE 도기헌](https://github.com/Dockerel)                                   |                                   [경북대 BE 석혜원](https://github.com/hyeywon)                                    |

| [<img src="https://avatars.githubusercontent.com/u/128658727?v=4" width="100px">](https://github.com/xkrwjdals) | [<img src="https://avatars.githubusercontent.com/u/114985804?v=4" width="100px">](https://github.com/yjhannn) |
|:---------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------:|
|                                   [경북대 BE 탁정민](https://github.com/xkrwjdals)                                    |                                   [경북대 BE 한영진](https://github.com/yjhannn)                                    |

## 🖥️배포 링크

http://talkak-fe.s3-website.ap-northeast-2.amazonaws.com

http://ec2-43-202-1-31.ap-northeast-2.compute.amazonaws.com


## 🤝그라운드 룰

[Ground Rule](https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=93d3274c7a8b4d159b38b16115c3ff42&pm=s)

## 📜커밋 컨벤션

[Commit Convention](https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=17afc129cb144bfea2fc3cea94ea153d&pm=s)

## 👨‍💻코딩 컨벤션

[Coding Convention](https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=833e57b063094194b6150558d89af6ec&pm=s)

## 📋API 명세서

[API Docs](https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=c1164ec45e4a485284644935bdb9a29f&pm=s)

## ❎에러코드 정의서

[ErrorCode Definition](https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=de999aae2f53421088e7edd875702089&pm=s)

## 🛠️테스트 결과 보고서

[Testing Report](https://quickest-asterisk-75d.notion.site/de33c852391d4e1599721de6136e9c3a?p=1243d1ecfbac437abbffece87f8f7c8a&pm=s)

## 유의사항
서버 가용량이 부족하여 동시 접속이 어려운 구조인 점 유의 부탁드립니다.
