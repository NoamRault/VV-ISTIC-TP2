# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

Apache Commons Collections

Vrai positif :

`commons-collections-master/src/main/java/org/apache/commons/collections4/collection/CompositeCollection.java:461: This for loop can be replaced by a foreach loop`

```java
for (final Iterator<E> it = iterator(); it.hasNext(); i++) {
    result[i] = it.next();
}
```

peut être remplacé par :

```java
for (E element : this) {
    result[i++] = element;
}
```

Faux positif :

`commons-collections-master/src/main/java/org/apache/commons/collections4/CollectionUtils.java:476: Avoid using implementation types like 'ArrayList'; use the interface instead`

ligne 473 : mergedList.trimToSize();

La méthode trimToSize() est uniquement disponible sur la classe ArrayList et pas sur l'interface List.
