## CMPE493 - Information Retrieval Project 3: PageRank for Identifying Central People in News Articles

#### About the Project
In this assignment, I have developed a PageRank-based method to identify the most important people occuring in news articles. The data.txt file is a plain text file containing an undirected and unweighted graph of social network of co-occurrence in news articles. The graph has been constructed by reading 3000 news articles from the Reuters-21578 corpus and identifying the person names. The vertices of the graph are defined as distinct people. An edge is constructed between two people if their names appear in the same news article.  I have implemented and run the Pagerank algorithm (the power iteration method) to determine the most central people in the co-occurrence graph. 


#### The names of the top 50 people:
1. reagan 

2. james-baker 

3. nakasone 

4. thatcher 

5. lyng 

6. stoltenberg 

7. Weinberger 

8. volcker 

9. howard-baker 

10. yeutter 

11. Gorbachev 

12. greenspan 

13. miyazawa 

14. Gephardt 

15. kohl 

16. poehl 

17. shultz 

18. ozal 

19. balladur 

20. Howard 

21. chirac 

22. lawson 

23. suharto 

24. papandreou 

25. Fitzwater 

26. Abe 

27. Mulroney 

28. bush 

29. sumita 

30. Roosevelt 

31. Moore 

32. Carter 

33. Ruder 

34. Schneider 

35. honda 

36. Bentsen 

37. saddam-hussein 

38. tamura 

39. Schlesinger 

40. Cochran 

41. john-paul 

42. Leahy 

43. Byrd 

44. Kuranari 

45. keating 

46. Reid 

47. Nixon 

48. Foley 

49. Yamamoto 

50. sprinkel