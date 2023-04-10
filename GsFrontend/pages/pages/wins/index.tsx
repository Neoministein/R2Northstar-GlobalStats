import { Column } from 'primereact/column';
import TopService from '../../../demo/service/TopService';
import TopResultTable from '../../../demo/components/TopResultTable';

const WinsPage = () => {

    return (
        <TopResultTable 
            title="Top Player Kills" 
            getGlobalRanking={(tags) => TopService.getTopWins(tags)}
            columns={
                [<Column header="Player Name" field="playerName"/>,
                <Column header="Wins" field="win" />]}/> 
    );
};

export default WinsPage;