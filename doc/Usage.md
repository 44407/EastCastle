# Using East Castle Sorting

## Simple Static Interface

East Castle sorting can be accessed statically via the Sorts class:
```
   int[] a = new int[] {5, 0, -7, 1};

   Sorts.sort(a);
```
This will use a default sorting implementation based on the type of data being sorted.

## Explicitly Obtaining a Specific Sort Implementation

More control can be gained by explicitly obtaining a sort implementation, and using that implementation. For instance, the below example obtains an implementation of Java's default system sorting:
```
   Sort sort;
   int[] a = new int[] {5, 0, -7, 1};
   
   sort = SystemSort.instance;
   sort.sort(a);
```

## Sorting Parameters

All East Castle sort methods allow sort range specification:
```
   int[] a = new int[] {5, 0, -7, 1};

   Sorts.sort(a, 1, 2);
```

In addition, East Castle sorting allows the specification of sort direction:
```
   int[] a = new int[] {5, 0, -7, 1};

   Sorts.sort(a, SortParameters.DEFAULT.order(Order.DESCENDING));
```

Direction and range can both be specified as follows:
```
   int[] a = new int[] {5, 0, -7, 1};

   Sorts.sort(a, SortParameters.DEFAULT.order(Order.DESCENDING).i0(1).i1(2));
```

## Sort Providers

Sort Providers provide sort implementations based on the type of data being sorted. For example, to obtain the default implementation for sorting integer arrays:
```
   Sort	sort;
   
   sort = Sorts.getSortForArrayOf(int.class)
```

Alternatively, we can obtain the Sort Provider by passing in the instance to be sorted:
```
   Sort	sort;
   int[] a = new int[] {5, 0, -7, 1};
   
   sort = sortProvider.getSortFor(a);
```

In addition, we can use our own specification via our own Sort Provider:
```
   MutableSortProvider sortProvider;
   Sort	sort;   

   // Create a sort provider that defaults to RadJSort
   sortProvider = new MutableSortProvider(RadJSort.defaultInstance);
   // Specify that for doubles, we use SystemSort
   sortProvider.put(double.class, SystemSort.instance);
   
   sort = sortProvider.getSortForArrayOf(int.class);    // will return RadJSort
   sort = sortProvider.getSortForArrayOf(double.class); // will return SystemSort   
```
