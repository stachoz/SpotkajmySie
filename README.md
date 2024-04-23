# Spotkajmy Się 
## Opis 

Program, który na podstawie kalendarzy dwóch osób oraz oczekiwanej długości spotkania przedstawi 
propozycję możliwych terminów spotkań.  
## Logika programu 
- Program analizuje kalendarze obu osób w celu znalezienia dostępności pracowników, tj. interwałów czasowych, 
  w których nie ma zaplanowanych żadnych spotkań.
- Następnie program identyfikuje części wspólne dostępnego czasu dla obu pracowników. 
  To oznacza interwały czasowe, w których obaj pracownicy są dostępni jednocześnie.
## Api Endpoint 
> `GET` calendar/meeting-time 

### Przykładowe żądania przy użyciu `curl`:

<pre>
curl --location --request GET 'localhost:8080/calendar/meeting-time' \
--header 'Content-Type: application/json' \
--data '{
    "meeting_duration": "00:30",
    "first_calendar": {
        "working_hours": {
            "start": "09:00",
            "end": "19:55"
        },
        "planned_meeting": [
            {
                "start": "09:00",
                "end": "10:30"
            },
            {
                "start": "12:00",
                "end": "13:00"
            },
            {
                "start": "16:00",
                "end": "18:00"
            }
        ]
    },
    "second_calendar": {
        "working_hours": {
            "start": "10:00",
            "end": "18:30"
        },
        "planned_meeting": [
            {
                "start": "10:00",
                "end": "11:30"
            },
            {
                "start": "12:30",
                "end": "14:30"
            },
            {
                "start": "14:30",
                "end": "15:00"
            },
            {
                "start": "16:00",
                "end": "17:00"
            }
        ]
    }
}'
</pre>

### Odpowiedź
Status 200 OK
<pre>
[
    {
        "start": "11:30",
        "end": "12:00"
    },
    {
        "start": "15:00",
        "end": "16:00"
    },
    {
        "start": "18:00",
        "end": "18:30"
    }
]
</pre>

### Żądanie z kalendarzami, które nie mają wolnego czasu
<pre>
curl --location --request GET 'localhost:8080/calendar/meeting-time' \
--header 'Content-Type: application/json' \
--data '{
    "meeting_duration": "00:30",
    "first_calendar": {
        "working_hours": {
            "start": "09:00",
            "end": "19:55"
        },
        "planned_meeting": [
            {
                "start": "09:00",
                "end": "19:55"
            }
        ]
    },
    "second_calendar": {
        "working_hours": {
            "start": "10:00",
            "end": "18:30"
        },
        "planned_meeting": [
            {
                "start": "10:00",
                "end": "18:30"
            }
        ]
    }
}'
</pre>
### Odpowiedź
Status 200 OK
<pre>
[]
</pre>

## Odpowiedź ze statusem Bad Request 400
Odpowiedzi ze statusem 400 należy się spodziewać, jeśli ciało żądania będzie niepoprawne, np
- pola będą zawierały wartości `null`
- czas będzie niepoprawnie sformatowany, np. zamiast `09:00`, będzie '9:00'

## Testy jednostkowe
Klasa `CalendarService` jest pokryta testami jednostkowymi, które uwzględniają następujące przypadki testowe:

- różnie pokrywające się interwały czasowe spotkań
- różna ilość zaplanowanych spotkań 
- całkowity brak czasu, aktualnie zaplanowane spotkania zajmują cały dzień
- osoby mają różne godziny pracy, ich godziny nie pokrywają się 
- osoby nie mają zaplanowanych spotkań
- osoby mają identyczne spotkania
- zróżnicowany oczekiwany czas spotkania włącznie ze zbyt długimi