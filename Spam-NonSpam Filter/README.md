## CMPE493 - Information Retrieval Project 2

#### About the Project
In this assignment, I have implemented a spam/non-spam filter using the Rocchio and the k-Nearest Neighbor (kNN) algorithms. I have used a subset of the Ling-Spam corpus[1] to train and test my system. You can find the training and test sets in the repository. 

Firstly I took each token from email message files. Next, I tokenize it by concatenating strings that contains hyphens, removing all non-character and non-numbers from word’s start and end positions, and make some clitics operations. Then, the terms are lowercased the  and saved it to my corpus.

#### Top Spam Words
adult with score : 4.006047259462496
click with score : 3.3637675360788104
email with score : 3.091238229751919
20 with score : 2.829247365576703
http with score : 2.771683549319141
earn with score : 2.7475597202868447
www with score : 2.721198251040258
address with score : 2.6836585182106916
com with score : 2.6498051858820375
phone with score : 2.6431808974161823
here with score : 2.632987791943719
call with score : 2.547407077794004
send with score : 2.5269975891593064
thank with score : 2.503791914169559
sex with score : 2.4898051728952617
visit with score : 2.4795733475372153
service with score : 2.4794286533308405
million with score : 2.469948770014848
degree with score : 2.4591030748519103
web with score : 2.4531546931061574

#### Top Legitimate Words
thank with score : 2.8946713946627987
anyone with score : 2.7569552365611174
linguist with score : 2.7170325923589873
interest with score : 2.6775541794779443
query with score : 2.674419715310789
work with score : 2.661619995290665
english with score : 2.5990662073997584
3 with score : 2.481141863152775
email with score : 2.4809643709216775
chomsky with score : 2.4430237994977557
re with score : 2.4408456324330627
reference with score : 2.401965781395403
please with score : 2.3967720633186866
linguistics with score : 2.3215665412924804
doe with score : 2.3213720915582603
many with score : 2.3097110591250156
information with score : 2.282841334116607
list with score : 2.2293919620177136
word with score : 2.2217770529215555
send with score : 2.214089972514972

#### Results
The results for classifcation algorithms are as follow:

|				|Precision	|Recall		|F-measure	|
| ------------- |:---------:| ---------:|----------:|
|Rocchio		|83.62%		|100%		|91.08%		|
|kNN for k=1	|89.3%		|87.08%		|88.18%		|
|kNN for k=3	|90.17%		|87.91%		|89.02%		|
|kNN for k=5	|90%		|94.5%		|92.27%		|
|kNN for k=7	|88.8%		|95.8%		|92.18%		|
|kNN for k=9	|88.12%		|95.83%		|91.81%		|

#### References
[1] Androutsopoulos, J. Koutsias, K.V. Chandrinos, George Paliouras, and C.D. Spyropoulos, “An Evaluation of Naive Bayesian
Anti-Spam Filtering”. In Potamias, G., Moustakis, V. and van Someren, M. (Eds.), Proceedings of the Workshop on Machine
Learning in the New Information Age, 11th European Conference on Machine Learning (ECML 2000), Barcelona, Spain, pp.
9-17, 2000.