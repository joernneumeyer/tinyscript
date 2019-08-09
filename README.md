# TinyScript
TinyScript (or tisc for short) is a very small scripting language - as the name suggests.
It is a little side project, so I can get a feel how to get going with programming/scripting language design and implementation.

As this project is meant to be used in any production system, it is unlikely that any serious and detailed documentation will be added.

## Features

### Data Types
The data types available are `string` and `number`.
A `string` is just text and a `number` always refers to a `double`.

### Arithmetic
TinyScript allows you to perform arithmetic, but without operator precedence.
To enforce precedence, just combine operations with parenthesis.

Supported operations:
- `+` - if both sides are of type `number`, an addition will be performed. Otherwise this operation will default to a `string` concatenation
- `*` - if both sides are of type `number`, a multiplication will be performed. Otherwise a `RuntimeError` will be thrown
- `-` - if both sides are of type `number`, a subtraction will be performed. Otherwise a `RuntimeError` will be thrown
- `/` - if both sides are of type `number`, a division will be performed. Otherwise a `RuntimeError` will be thrown
- `**` - if both sides are of type `number`, the left number will be raised to the power of the right number. Otherwise a `RuntimeError` will be thrown

### Variables
Variable names have to start with a `$`.
A variable name may contain the letters A-Z (in upper and lower) and underscores (`_`).
New variable values may change the type of a variable.

A variable always has to be initialized with a value.
So, a variable initialization looks like this:

```
$name := "John Doe"
$age := 22
```

You may also assign the result of an expression to a variable:

```
$age := 2 ** 3
```

### Functions
The naming rules for functions are the same as they are for variables.
This example illustrates the definition and usage of a function:

```
#circle_area $radius
  3.141592654 * ($radius ** 2)
#circle_area

$radius := 1

$area := [circle_area:$radius]
```

A function definition starts with a hash (`#`) and is followed by the name of the function.
After the name follows a listing of the parameters of the function.
The function body ends with the same name declaration as the function header started with.

The function body may contain any regular statements, where the last statement in the function will be its return value.

Calling a function works by placing a block of brackets `[]`, where the first part is the name of the function, followed by a list of its arguments, which are separated by colons `:`.

A function may also not take any parameters, so you would just wrap the function's name in brackets.

It is also possible to use a function call as a parameter for another function call.

## License
This project is licensed under [version 3 of the GNU General Public License](./COPYING).