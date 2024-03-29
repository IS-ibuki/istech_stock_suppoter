# (株)アイエステック 在庫管理システム
![アイエステック_在庫管理_在庫一覧](https://user-images.githubusercontent.com/81798148/223775599-b540a971-75b8-4775-a133-514f9384c0bd.png)


## 開発経緯
両親が自営業を営んでいて、月末に行われる在庫の棚卸が大変であるという声を聞き、このWebアプリケーションを開発することした。

## 機能詳細
- 製品情報、注文書、顧客情報、構成要素のCRUD機能
- 注文書に基づいた製品の入出庫処理機能
- CSV形式のファイルを利用した製品、構成要素の一括登録機能
- 製品、注文書のソート・検索機能

## 開発者のこだわりポイント
- 自作アノテーションの実装  
コードの可読性・保守性低下を防ぐために、バリデーション用の自作アノテーションを実装した。
- メソッドの役割をシンプルに  
結合度を弱くすることで、この関数修正したから他のも修正する・・・という作業をできる限りなくすようにしました。

## AWS構成図
![istexh_aws drawio (3)](https://user-images.githubusercontent.com/81798148/225564657-c77a09d0-ef36-47f1-b238-a704efd92dd6.png)   

## 画面一覧   
顧客情報等が含まれているため、本番環境のURLは非公開にしてあります。  
※開発環境でのキャプチャ画像になります。
### 製品一括登録画面   
![スクリーンショット (7)](https://user-images.githubusercontent.com/81798148/225572696-dfa66e21-5971-425b-b8ce-60df3caf3c1f.png)   

### 製品詳細画面   
![スクリーンショット (8)](https://user-images.githubusercontent.com/81798148/225572806-00907be1-63dd-4f09-a559-8520968b572b.png)   

## 今後修正すべき点
- ゲストユーザの追加  
お試しで使いたい方に向けて、ゲスト用アカウントの追加が必要
- UIの修正  
誰でも使える・使いやすいインターフェースが必要
- CSV形式以外での一括登録機能  
Excel等でも対応可能にする。Apache POIを使用予定。  
https://poi.apache.org/ 
- 非効率な動作をしている機能の修正  
特に製品の入出庫処理に関して、もっと効率良い方法があるはず。
- リファクタリング作業  
